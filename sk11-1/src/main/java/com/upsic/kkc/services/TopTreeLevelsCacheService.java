package com.upsic.kkc.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;
import com.upsic.kkc.models.TopTreeLevelsCache;
import com.upsic.kkc.models.TopTreeLevelsCacheItem;
import com.upsic.kkc.repository.TopTreeLevelsCacheItemRepository;
import com.upsic.kkc.repository.TopTreeLevelsCacheRepository;
import com.upsic.kkc.services.mapper.TopTreeLevelsCacheItemMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TopTreeLevelsCacheService {

    private Sk11Configuration sk11Configuration;

    private TopTreeLevelsCacheRepository topTreeLevelsCacheRepository;

    private TopTreeLevelsCacheItemRepository topTreeLevelsCacheItemRepository;

    private TopTreeLevelsCacheItemMapper topTreeLevelsCacheItemMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean tryStartUpdating() {
        topTreeLevelsCacheRepository.lockTable();

        List<TopTreeLevelsCache> all = topTreeLevelsCacheRepository.findAll();

        if (isNeedValidation(all.stream().map(TopTreeLevelsCache::getLastUpdateTime).max(Instant::compareTo))) {
            Instant now = Instant.now();
            if (all.isEmpty()) {
                TopTreeLevelsCache cache = new TopTreeLevelsCache();
                cache.setId(System.currentTimeMillis());
                // если кеша не было, то это всегда инициализация
                cache.setInitializing(true);
                cache.setUpdating(true);
                cache.setLastUpdateStartedTime(now);
                cache.setLastUpdateTime(now);
                topTreeLevelsCacheRepository.saveAndFlush(cache);
                return true;
            } else if (isIdleOrTimedOut(now, all)) {
                all.forEach(c -> {
                    // если у нас кеш уже протух, то это инициализация, иначе - апдейт
                    c.setInitializing(c.getLastUpdateTime().isBefore(now));
                    c.setLastUpdateStartedTime(now);
                    c.setUpdating(true);
                });
                topTreeLevelsCacheRepository.saveAllAndFlush(all);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public int updateCaches(List<Pair<UUID, Sk11TreeDto>> caches) {

        long start = System.currentTimeMillis();

        int result = 0;

        topTreeLevelsCacheRepository.lockTable();

        topTreeLevelsCacheItemRepository.removeAll();

        log.info("Remove time: " + (System.currentTimeMillis() - start));

        for (Pair<UUID, Sk11TreeDto> cache : caches) {
            result += updateCache(cache.getSecond(), cache.getFirst());
        }

        log.info("Update time: " + (System.currentTimeMillis() - start));

        Instant now = Instant.now();
        List<TopTreeLevelsCache> all = topTreeLevelsCacheRepository.findAll();
        all.forEach(c -> c.setLastUpdateTime(now));
        topTreeLevelsCacheRepository.saveAllAndFlush(all);

        log.info("Total time: " + (System.currentTimeMillis() - start));

        return result;
    }

    @Transactional
    public void finishUpdating() {
        topTreeLevelsCacheRepository.lockTable();
        List<TopTreeLevelsCache> all = topTreeLevelsCacheRepository.findAll();
        all.forEach(c -> {
            c.setInitializing(false);
            c.setUpdating(false);
        });
        topTreeLevelsCacheRepository.saveAllAndFlush(all);
    }

    private boolean isNeedValidation(Optional<Instant> lastUpdateDateOptional) {
        return lastUpdateDateOptional.map(lastUpdateDate -> lastUpdateDate
                // добавляем дату протухания
                .plus(sk11Configuration.getExpiresAfterDays(), ChronoUnit.DAYS)
                // отнимаем дни до протухания, так как обновиться нужно заранее
                .minus(sk11Configuration.getInvalidatesBeforeHours(), ChronoUnit.HOURS)
                // добавляем фактор случайности, что бы все инстансы не обновлялись одновременно
                .minus(new Random().nextInt(sk11Configuration.getRandomFactorMinutes()), ChronoUnit.MINUTES)
        ).map(expirationDate -> expirationDate.isBefore(Instant.now())).orElse(true);
    }

    @Transactional
    public boolean isInitializingOrOutdated() {
        List<TopTreeLevelsCache> all = topTreeLevelsCacheRepository.findAll();
        return all.isEmpty()
                || all.stream().anyMatch(TopTreeLevelsCache::getInitializing)
                || all.stream().anyMatch(t -> t.getLastUpdateTime().plus(
                sk11Configuration.getExpiresAfterDays(), ChronoUnit.DAYS).isBefore(Instant.now()));
    }

    private boolean isIdleOrTimedOut(Instant now, List<TopTreeLevelsCache> all) {
        // проверим статус
        if (all.stream().noneMatch(TopTreeLevelsCache::getUpdating)) {
            // если статуса update нет, то это idle
            return true;
        }

        // если статус update, то проверяем таймаут
        Optional<Instant> lastUpdateStarted = all.stream().map(TopTreeLevelsCache::getLastUpdateStartedTime)
                .filter(Objects::nonNull).max(Instant::compareTo);
        if (lastUpdateStarted.isPresent() && lastUpdateStarted.get().isBefore(
                now.minusSeconds(sk11Configuration.getLongCacheUpdateTimeoutSeconds()))) {
            log.warn("Cache update procedure is timed out! Started at {}", lastUpdateStarted.get());
            return true;
        }

        return false;
    }

    /**
     * Добавление в кеш корневого элемента
     *
     * @param treeElem - дерево элементов
     */
    private int updateCache(Sk11TreeDto treeElem, UUID model) {
        TopTreeLevelsCache cache = topTreeLevelsCacheRepository.findFirstBy();

        TopTreeLevelsCacheItem root = new TopTreeLevelsCacheItem();
        root.setCache(cache);
        root.setModel(model);
        topTreeLevelsCacheItemMapper.fillEntity(treeElem, root);
        topTreeLevelsCacheItemRepository.save(root);

        return updateCache(treeElem, cache, model);

    }

    /**
     * Добавление в кеш всех элементов дерева, кроме корневого
     *
     * @param treeElem - дерево элементов
     * @param cache    - сущность кеша
     */
    private int updateCache(Sk11TreeDto treeElem, TopTreeLevelsCache cache, UUID model) {

        List<Sk11TreeDto> children = treeElem.getChildren();
        int i = 0;
        if (children != null && !children.isEmpty()) { //складываем в кеш детей
            for (Sk11TreeDto child : children) {
                i++;
                TopTreeLevelsCacheItem item = new TopTreeLevelsCacheItem();
                item.setCache(cache);
                item.setModel(model);
                item.setParent(treeElem.getUid());
                topTreeLevelsCacheItemMapper.fillEntity(child, item);
                topTreeLevelsCacheItemRepository.save(item);
                i = i + updateCache(child, cache, model);
            }
        }
        return i;
    }

    public List<Sk11TreeNodeDto> getTreeNodeChildren(String modelUid, String objectUid) {
        UUID uuid = objectUid != null ? UUID.fromString(objectUid) : null;
        return topTreeLevelsCacheItemMapper.toDto(
                topTreeLevelsCacheItemRepository.findAllByParentAndModelOrderById(uuid, UUID.fromString(modelUid)));
    }


}
