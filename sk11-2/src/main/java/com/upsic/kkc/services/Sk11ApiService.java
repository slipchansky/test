package com.upsic.kkc.services;

import java.util.List;

import com.upsic.kkc.dto.sk11api.LuaScriptDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;


public interface Sk11ApiService {

    /**
     * Получение доступных моделей api СК-11
     *
     * @return список моделей
     */
    List<Sk11ModelDto> getModels();

    /**
     * Выполнение заданного LUA-скрипта
     *
     * @param modelUid     - идентификатор модели
     * @param luaScriptDto - объект, содержащий LUA-скрипт
     * @param clazz        - класс для ParameterizedTypeReference
     * @param <T>          - тип объектов в результирующем списке
     * @return - список объектов указанного типа
     */
    <T> List<T> executeLuaScript(String modelUid, LuaScriptDto luaScriptDto, Class<T> clazz);


}
