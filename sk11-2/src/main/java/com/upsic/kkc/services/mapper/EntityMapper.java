package com.upsic.kkc.services.mapper;

import java.util.List;

public interface EntityMapper<D, E> {

    D toDto(E entity);

    List<D> toDto(Iterable<E> entityList);

}