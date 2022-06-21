package com.upsic.kkc.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.upsic.kkc.dto.sk11api.FiltersDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class Sk11ShadowIntegrationService {

    private final Sk11IntegrationService sk11IntegrationService;

    private final TopTreeLevelsCacheService topTreeLevelsCacheService;

    /**
     * Попытка обновления кеша
     */
    public void calculateCacheForFirstLevels() {
        if (topTreeLevelsCacheService.tryStartUpdating()) {
            try {
                log.info("start compute scheduled cache");
                List<Sk11ModelDto> models = sk11IntegrationService.getModels();
                List<Pair<UUID, Sk11TreeDto>> caches = new ArrayList<>(models.size());
                models.forEach(sk11ModelDto -> {
                    try {
                        long start = System.currentTimeMillis();
                        Sk11TreeDto tree = sk11IntegrationService.getTreeForLevel(sk11ModelDto.getModelUid().toString());
                        fillFilters(tree);
                        caches.add(Pair.of(sk11ModelDto.getModelUid(), tree));
                        long end = System.currentTimeMillis();
                        log.debug("compute scheduled cache for model {}elements, time {} s", sk11ModelDto.getModelUid().toString(), (end - start) / 1000);
                    } catch (Exception e) {
                        log.error("compute scheduled cache for model " + sk11ModelDto.getModelUid().toString() + " has error: " + e.getMessage(), e);
                    }
                });
                int size = topTreeLevelsCacheService.updateCaches(caches);
                log.info("end compute scheduled cache. Total items: {}", size);
            } catch (Exception e) {
                log.error("compute scheduled cache has global error: " + e.getMessage(), e);
            } finally {
                topTreeLevelsCacheService.finishUpdating();
            }
        }

    }
    
    /**
     * Заполняем фильтры для всех элементов, основываясь на фильтрах последних элементов
     *
     * @param treeElem- дерево элементов
     * @return промежуточные данные для рекурсии
     */
    private FiltersDto fillFilters(Sk11TreeDto treeElem) {
        List<Sk11TreeDto> children = treeElem.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Sk11TreeDto child : children) {
                FiltersDto filters = fillFilters(child);
                if (filters != null) {
                    treeElem.setFilters(treeElem.getFilters() == null ? new FiltersDto(new HashSet<>(), new HashSet<>()) : treeElem.getFilters()); // создаем новый объект если null
                    treeElem.getFilters().getTypeFilter().addAll(filters.getTypeFilter());
                    treeElem.getFilters().getCategoryUidFilter().addAll(filters.getCategoryUidFilter());
                }
            }
        }
        return treeElem.getFilters();
    }

}
