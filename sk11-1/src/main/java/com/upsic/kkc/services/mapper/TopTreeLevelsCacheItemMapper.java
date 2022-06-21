package com.upsic.kkc.services.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.upsic.kkc.configuration.MapStructConfiguration;
import com.upsic.kkc.dto.sk11api.FiltersDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;
import com.upsic.kkc.models.TopTreeLevelsCacheItem;

import java.util.List;

@Mapper(config = MapStructConfiguration.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TopTreeLevelsCacheItemMapper extends EntityMapper<Sk11TreeNodeDto, TopTreeLevelsCacheItem>, FillEntityMapper<Sk11TreeDto, TopTreeLevelsCacheItem>  {

    
    @Override
    List<Sk11TreeNodeDto> toDto(Iterable<TopTreeLevelsCacheItem> entity);

    @Mapping(target = "filters", expression = "java(toFiltersDto(entity))")
    Sk11TreeNodeDto toDto(TopTreeLevelsCacheItem entity);

    FiltersDto toFiltersDto(TopTreeLevelsCacheItem entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "cache", ignore = true)
    @Mapping(target = "typeFilter", source = "filters.typeFilter")
    @Mapping(target = "categoryUidFilter", source = "filters.categoryUidFilter")
    void fillEntity(Sk11TreeDto dto, @MappingTarget TopTreeLevelsCacheItem target);

}
