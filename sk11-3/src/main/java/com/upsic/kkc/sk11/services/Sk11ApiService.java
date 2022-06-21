package com.upsic.kkc.sk11.services;



import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.upsic.kkc.sk11.dto.AnswerDto;
import com.upsic.kkc.sk11.dto.LuaScriptDto;
import com.upsic.kkc.sk11.dto.TelemetryDeleteDto;
import com.upsic.kkc.sk11.dto.TelemetryValueDto;
import com.upsic.kkc.sk11.dto.TelemetryValueIntervalDto;

/**
 * Сервис реализует обращения к api СК-11
 */
public interface Sk11ApiService {


    /**
     * Получение значений телеметрии в числовом формате
     *
     * @param uids      список идентификаторов телеметрии
     * @param timestamp дата на которую нужны данные
     * @return список телеметрии по указанным UIDS
     */
    List<TelemetryValueDto> getMeasurementValuesDouble(List<UUID> uids, Instant timestamp);

    /**
     * Отправка значений телеметрии в СК-11 в числовом формате
     *
     * @param telemetryValues список значений телеметрии
     */
    AnswerDto saveMeasurementValuesDouble(List<TelemetryValueDto> telemetryValues);

    /**
     * Удаление значений телеметрии в СК-11
     */
    AnswerDto deleteMeasurementValues(List<TelemetryDeleteDto> selectors);

    /**
     * Получение списка значений телеметрии за интервал времени
     */
    TelemetryValueIntervalDto getMeasurementValuesInInterval(UUID measurementValueUid, Instant fromTimeStamp, Instant toTimeStamp);

    /**
     * Выполнение заданного LUA-скрипта
     *
     * @param luaScriptDto - объект, содержащий LUA-скрипт
     * @param clazz        - класс для ParameterizedTypeReference
     * @param <T>          - тип объектов в результирующем списке
     * @return - список объектов указанного типа
     */
    <T> List<T> executeLuaScript(LuaScriptDto luaScriptDto, Class<T> clazz);


}
