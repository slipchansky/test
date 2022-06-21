package com.upsic.kkc.services;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.dto.sk11api.LuaScriptDto;
import com.upsic.kkc.dto.sk11api.SearchResultsDto;
import com.upsic.kkc.dto.sk11api.Sk11CheckAddingLevelDto;
import com.upsic.kkc.dto.sk11api.Sk11FullMeasurementValueDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.dto.sk11api.Sk11NodeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;
import com.upsic.kkc.exceptions.TopTreeLevelCacheException;
import com.upsic.kkc.utils.AppConstants;
import com.upsic.kkc.utils.CollectionsUtils;
import com.upsic.kkc.utils.SortingUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static com.upsic.kkc.utils.AppConstants.Cache.*;
import static com.upsic.kkc.utils.AppConstants.ErrorDescriptions.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class Sk11IntegrationService {

    private final Sk11ApiService sk11ApiService;

    private final LuaScriptService luaScriptService;

    private final Sk11Configuration sk11Configuration;

    private final TopTreeLevelsCacheService topTreeLevelsCacheService;

    /**
     * Получение доступных моделей api СК-11
     *
     * @return список моделей
     */
    @Cacheable(MODELS_CACHE)
    public List<Sk11ModelDto> getModels() {
        return sk11ApiService.getModels();
    }

    @Cacheable(CATEGORIES_CACHE)
    @Transactional(readOnly = true)
    public List<Sk11NodeDto> getAllCategories(String modelUid) {
        LuaScriptDto luaScriptDto = luaScriptService.getCategoriesScript();
        return sk11ApiService.executeLuaScript(modelUid, luaScriptDto, Sk11NodeDto.class);
    }

    /**
     * Получаем кусочек дерева (с рекурсией) для сохранения в БД
     * @param modelUid идентификатор модели
     * @return дерево
     */
    @Transactional(readOnly = true)
    public Sk11TreeDto getTreeForLevel(String modelUid) {
        LuaScriptDto luaScriptDto = luaScriptService.getTreeForLevelScript(sk11Configuration.getPreCachedLevels());
        List<Sk11TreeDto> result = sk11ApiService.executeLuaScript(modelUid, luaScriptDto, Sk11TreeDto.class);
        return result.stream().findFirst().orElse(null);
    }

    /**
     * Получение дочерних элементов для элемента дерева
     *
     * @param modelUid  - идентификатор модели
     * @param objectUid - идентификатор объекта
     * @return список дочерних элементов
     */
    @Cacheable(value = CHILDREN_CACHE, key = "#modelUid + #objectUid")
    @Transactional(readOnly = true)
    public List<Sk11TreeNodeDto> getTreeNodeChildren(String modelUid, String objectUid) {

        // уточним состояние кеша, если он инициализируется или просрочен, то выбросим исключение
        if (topTreeLevelsCacheService.isInitializingOrOutdated()) {
            throw new TopTreeLevelCacheException(AppConstants.ErrorDescriptions.CACHE_IS_INITIALIZED_OR_OUTDATED);
        }

        // проверим, есть ли такое в БД иначе пойдем в СК11
        List<Sk11TreeNodeDto> longCacheChildren = topTreeLevelsCacheService.getTreeNodeChildren(modelUid, objectUid);
        List<Sk11TreeNodeDto> result = CollectionsUtils.ofEmpty(longCacheChildren).orElseGet(() -> getTreeNodeChildrenWithFilterInternal(modelUid, objectUid));
        result.sort(SortingUtils.SK11_TREE_NODE_SORTING);
        return result;
    }

    /**
     * Поиск в дереве по UID
     *
     * @param modelUid  - идентификатор модели
     * @param objectUid - идентификатор объекта
     * @return список путей для найденных элементов
     */
    @Transactional(readOnly = true)
    public SearchResultsDto searchByUid(String modelUid, String objectUid) {
        LuaScriptDto luaScriptDto = luaScriptService.getSearchByUidScript(objectUid);
        List<SearchResultsDto> result = sk11ApiService.executeLuaScript(modelUid, luaScriptDto, SearchResultsDto.class);
        return result.stream().findFirst().orElse(new SearchResultsDto(true, Collections.emptyList()));
    }

    /**
     * Поиск в дереве по UID
     *
     * @param modelUid   - идентификатор модели
     * @param objectName - часть имени объекта
     * @return список путей для найденных элементов
     */
    @Transactional(readOnly = true)
    public SearchResultsDto searchByName(String modelUid, String objectName) {
        LuaScriptDto luaScriptDto = luaScriptService.getSearchByNameScript(objectName);
        List<SearchResultsDto> result = sk11ApiService.executeLuaScript(modelUid, luaScriptDto, SearchResultsDto.class);
        return result.stream().findFirst().orElse(new SearchResultsDto(true, Collections.emptyList()));
    }

    /**
     * Получение дочерних элементов для элемента дерева с полным обходом дерева
     *
     * @param modelUid  - идентификатор модели
     * @param objectUid - идентификатор объекта
     * @return список дочерних элементов
     */
    private List<Sk11TreeNodeDto> getTreeNodeChildrenWithFilterInternal(String modelUid, String objectUid) {
        LuaScriptDto luaScriptDto = luaScriptService.getTreeNodeChildrenWithFiltersScript(objectUid);
        return sk11ApiService.executeLuaScript(modelUid, luaScriptDto, Sk11TreeNodeDto.class);
    }

    /**
     * Получение измерений и их параметров для элемента дерева
     *
     * @param modelUid   - идентификатор модели
     * @param objectUids - идентификаторы объектов
     * @return список дочерних элементов
     */
    @Transactional(readOnly = true)
    public List<Sk11FullMeasurementValueDto> getMeasurementValuesForNode(UUID modelUid, List<UUID> objectUids, SortedSet<String> filterTelemetryTypes, SortedSet<UUID> filterCategoryUids) {
        LuaScriptDto luaScriptDto = luaScriptService.getMeasurementValuesForNode(objectUids, filterTelemetryTypes, filterCategoryUids);
        return sk11ApiService.executeLuaScript(modelUid.toString(), luaScriptDto, Sk11FullMeasurementValueDto.class);
    }

    /**
     * Проверка по уровню в дереве. Можно ли обрабатывать объект с заданным objectUid
     *
     * @param modelUid  - идентификатор модели
     * @param objectUid - идентификатор объекта
     * @return результат проверки
     */
    @Transactional
    public boolean checkAddingLevel(UUID modelUid, UUID objectUid) {
        LuaScriptDto luaScriptDto = luaScriptService.getCheckAddingLevelScript(objectUid);
        List<Sk11CheckAddingLevelDto> result = sk11ApiService.executeLuaScript(modelUid.toString(), luaScriptDto, Sk11CheckAddingLevelDto.class);
        return result.stream().findFirst().map(Sk11CheckAddingLevelDto::getCorrect).orElse(false);
    }

}
