package com.upsic.kkc.sk11.services.mapper;

import org.mapstruct.MappingTarget;

public interface FillEntityMapper<D, E> {

    void fillEntity(D dto, @MappingTarget E target);
}