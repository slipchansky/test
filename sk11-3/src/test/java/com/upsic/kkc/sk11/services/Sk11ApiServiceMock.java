package com.upsic.kkc.sk11.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.upsic.kkc.sk11.data.GouData;
import com.upsic.kkc.sk11.dto.AnswerDto;
import com.upsic.kkc.sk11.dto.LuaScriptDto;
import com.upsic.kkc.sk11.dto.PowerGenTelemetryGuidDto;
import com.upsic.kkc.sk11.dto.QReserveTelemetryGuidDto;
import com.upsic.kkc.sk11.dto.Sk11ModelDto;
import com.upsic.kkc.sk11.dto.TelemetryDeleteDto;
import com.upsic.kkc.sk11.dto.TelemetryValueDto;
import com.upsic.kkc.sk11.dto.TelemetryValueIntervalDto;
import com.upsic.kkc.sk11.enums.GouType;
import com.upsic.kkc.sk11.enums.PowerGenType;
import com.upsic.kkc.sk11.enums.QReserveType;
import com.upsic.kkc.sk11.services.utils.DataTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
public class Sk11ApiServiceMock implements Sk11ApiService {

    private List<GouData> gouDataList = DataTestUtils.getGouDataList();

    private Double telemetryValue = Math.random();

    private Long qCode;

    public void initMock(List<GouData> gouDataList, Double telemetryValue, Long qCode) {
        this.gouDataList = gouDataList;
        this.telemetryValue = telemetryValue;
        this.qCode = qCode;
    }

    @Override
    public List<TelemetryValueDto> getMeasurementValuesDouble(List<UUID> uids, Instant timestamp) {
        return uids.stream()
                .map(uuid -> new TelemetryValueDto(uuid,
                        Instant.now(),
                        Instant.now(),
                        null,
                        telemetryValue))
                .collect(Collectors.toList());
    }

    @Override
    public AnswerDto saveMeasurementValuesDouble(List<TelemetryValueDto> telemetryValues) {
        return new AnswerDto();
    }

    @Override
    public AnswerDto deleteMeasurementValues(List<TelemetryDeleteDto> selectors) {
        return new AnswerDto();
    }

    @Override
    public TelemetryValueIntervalDto getMeasurementValuesInInterval(UUID measurementValueUid, Instant fromTimeStamp, Instant toTimeStamp) {
        TelemetryValueIntervalDto result = new TelemetryValueIntervalDto();
        List<TelemetryValueDto> values = new ArrayList<>();
        TelemetryValueDto telemetry1 = new TelemetryValueDto(measurementValueUid, fromTimeStamp, fromTimeStamp, null, telemetryValue);
        TelemetryValueDto telemetry2 = new TelemetryValueDto(measurementValueUid, toTimeStamp, toTimeStamp, null, telemetryValue * 2);
        values.add(telemetry1);
        values.add(telemetry2);
        result.setValue(values);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> executeLuaScript(LuaScriptDto luaScriptDto, Class<T> clazz) {
        if (clazz.equals(PowerGenTelemetryGuidDto.class)) {
            return (List<T>) executeBlockGenTelemetryGuidsScript(luaScriptDto);
        }
        if (clazz.equals(QReserveTelemetryGuidDto.class)) {
            return (List<T>) executeQReserveTelemetryGuidsScript(luaScriptDto);
        }
        return Collections.emptyList();
    }

    /**
     * Эмулируем выполнение скрипта для получения PowerGenTelemetryGuidDto
     */
    private List<PowerGenTelemetryGuidDto> executeBlockGenTelemetryGuidsScript(LuaScriptDto luaScriptDto) {
        List<PowerGenTelemetryGuidDto> result = new ArrayList<>();
        List<UUID> rgeUids = gouDataList.stream()
                .map(GouData::getUid)
                .collect(Collectors.toList());
        for (UUID rgeUid : rgeUids) {
            if (luaScriptDto.getLuaScript().toLowerCase().contains(rgeUid.toString().toLowerCase())) {
                result.add(new PowerGenTelemetryGuidDto(rgeUid, UUID.randomUUID(), PowerGenType.PGEN));
                result.add(new PowerGenTelemetryGuidDto(rgeUid, UUID.randomUUID(), PowerGenType.QGEN));
            }
        }
        return result;
    }

    /**
     * Эмулируем выполнение скрипта для получения QReserveTelemetryGuidDto
     */
    private List<QReserveTelemetryGuidDto> executeQReserveTelemetryGuidsScript(LuaScriptDto luaScriptDto) {
        List<QReserveTelemetryGuidDto> result = new ArrayList<>();
        List<UUID> gouUids = gouDataList.stream()
                .filter(gouData -> gouData.getType() == GouType.NBLOCK)
                .map(GouData::getUid)
                .collect(Collectors.toList());
        for (UUID gouUid : gouUids) {
            if (luaScriptDto.getLuaScript().toLowerCase().contains(gouUid.toString().toLowerCase())) {
                result.add(new QReserveTelemetryGuidDto(gouUid, UUID.randomUUID(), QReserveType.LOAD_RESERVE));
                result.add(new QReserveTelemetryGuidDto(gouUid, UUID.randomUUID(), QReserveType.UNLOAD_RESERVE));
            }
        }
        return result;
    }

    @Override
    public List<Sk11ModelDto> getModels() {
        return Arrays.asList(Sk11ModelDto.builder()
                .modelCanonicalName("test")
                .modelDisplayName("test")
                .modelUid(UUID.randomUUID())
                .profileVersion("1")
                .features(Collections.emptyList())
                .build()
                );
    }
}
