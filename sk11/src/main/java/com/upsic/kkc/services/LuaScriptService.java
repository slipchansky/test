package com.upsic.kkc.services;

import java.util.List;
import java.util.SortedSet;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.upsic.kkc.dto.sk11api.LuaScriptDto;


public interface LuaScriptService {

    LuaScriptDto getTreeForLevelScript(Integer level);

    LuaScriptDto getTreeNodeChildrenWithFiltersScript(String objectUid);

    LuaScriptDto getCategoriesScript();

    LuaScriptDto getMeasurementValuesForNode(List<UUID> objectUids, SortedSet<String> filterTelemetryTypes,
            SortedSet<UUID> filterCategoryUids);

    LuaScriptDto getCheckAddingLevelScript(UUID objectUid);

    LuaScriptDto getSearchByUidScript(String objectUid);

    LuaScriptDto getSearchByNameScript(String objectName);

}