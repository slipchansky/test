package com.upsic.kkc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Утилиты для работы с коллекциями.
 */
public final class CollectionsUtils {

    private CollectionsUtils() {
    }

    /**
     * Возвращает опционал заполненный в зависимости от того, есть ли в списке элементы.
     *
     * @param list список
     * @param <T>  Элементы списка
     * @return Optional
     */
    public static <T> Optional<List<T>> ofEmpty(List<T> list) {
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list);
    }

    /**
     * Создает список если он не создан
     * @param list список
     * @param <T> Элементы списка
     * @return список
     */
    public static <T> List<T> createIfNull(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }
}
