package com.upsic.kkc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import com.upsic.kkc.dto.sk11api.FiltersDto;
import com.upsic.kkc.dto.sk11api.Sk11ChannelDto;
import com.upsic.kkc.dto.sk11api.Sk11FullMeasurementValueDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;
import com.upsic.kkc.dto.sk11api.Sk11TelemetryDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeDto;
import com.upsic.kkc.dto.sk11api.Sk11TreeNodeDto;
import com.upsic.kkc.dto.sk11api.Sk11UnitDto;
import com.upsic.kkc.dto.sk11api.Sk11ValuesSetDto;
import com.upsic.kkc.dto.sk11api.Sk11ValuesSetValueDto;
import com.upsic.kkc.models.enums.Sk11MeasurementType;

public class Sk11DtoTestUtils {

    public static Sk11TreeNodeDto createSk11ThreeNodeDto(Consumer<Sk11TreeNodeDto> consumer) {
        Sk11TreeNodeDto sk11TreeNodeDto = new Sk11TreeNodeDto();
        sk11TreeNodeDto.setUid(UUID.randomUUID());
        sk11TreeNodeDto.setName("name");
        sk11TreeNodeDto.setObjectType("type");
        consumer.accept(sk11TreeNodeDto);
        return sk11TreeNodeDto;
    }
    
    public static Sk11TreeNodeDto createSk11ThreeNodeDto() {
        return createSk11ThreeNodeDto(dto -> {
        });
    }


    public static Sk11TreeNodeDto createSk11TreeNodeDto(Sk11TreeDto dto) {
        return createSk11ThreeNodeDto(sk11TreeNodeDto -> {
            sk11TreeNodeDto.setUid(dto.getUid());
            sk11TreeNodeDto.setName(dto.getName());
            sk11TreeNodeDto.setObjectType(dto.getObjectType());
            sk11TreeNodeDto.setFilters(dto.getFilters());
        });
    }

    public static Sk11ModelDto createSk11ModelDto() {
        return createSk11ModelDto(dto -> {
        });
    }

    public static Sk11ModelDto createSk11ModelDto(Consumer<Sk11ModelDto> consumer) {
        Sk11ModelDto dto = new Sk11ModelDto();
        dto.setModelUid(UUID.randomUUID());
        dto.setModelDisplayName("disp_name");
        dto.setModelCanonicalName("can_name");
        consumer.accept(dto);
        return dto;
    }

    public static Sk11TreeDto createSk11TreeDto() {
        return createSk11TreeDto(c -> {
        });
    }

    public static Sk11TreeDto createSk11TreeDto(Consumer<Sk11TreeDto> consumer) {
        Sk11TreeDto sk11TreeDto = new Sk11TreeDto();
        sk11TreeDto.setName(UUID.randomUUID().toString());
        sk11TreeDto.setUid(UUID.randomUUID());
        sk11TreeDto.setObjectType("type");

        FiltersDto filtersDto = new FiltersDto();
        filtersDto.setTypeFilter(Sets.newHashSet("type", "type-2"));
        filtersDto.setCategoryUidFilter(Sets.newHashSet(UUID.randomUUID(), UUID.randomUUID()));
        sk11TreeDto.setFilters(filtersDto);

        sk11TreeDto.setChildren(new ArrayList<>(3));
        for (int i = 0; i < 3; i++) {
            Sk11TreeDto subSk11TreeDto = new Sk11TreeDto();
            subSk11TreeDto.setName(UUID.randomUUID().toString());
            subSk11TreeDto.setUid(UUID.randomUUID());
            subSk11TreeDto.setObjectType("type-" + i);
            FiltersDto subFiltersDto = new FiltersDto();
            subFiltersDto.setTypeFilter(Sets.newHashSet());
            subFiltersDto.setCategoryUidFilter(Sets.newHashSet());
            subSk11TreeDto.setFilters(subFiltersDto);
            sk11TreeDto.getChildren().add(subSk11TreeDto);
        }

        consumer.accept(sk11TreeDto);
        return sk11TreeDto;
    }

    public static Sk11FullMeasurementValueDto createSk11FullMeasurementValueDto() {
        return createSk11FullMeasurementValueDto(dto -> {
        });
    }

    public static Sk11FullMeasurementValueDto createSk11FullMeasurementValueDto(Consumer<Sk11FullMeasurementValueDto> consumer) {

        Sk11FullMeasurementValueDto measurementValueDto = new Sk11FullMeasurementValueDto();
        measurementValueDto.setUid(UUID.randomUUID());
        measurementValueDto.setName("name");
        measurementValueDto.setObjectType("type");
        measurementValueDto.setCategory(createSk11ThreeNodeDto());
        measurementValueDto.setEnergyObject(createSk11ThreeNodeDto());
        measurementValueDto.setUnit(createSk11UnitDto());
        measurementValueDto.setMeasurementType(Sk11MeasurementType.ANALOG.getSk11Value());
        consumer.accept(measurementValueDto);
        return measurementValueDto;
    }

    public static Sk11UnitDto createSk11UnitDto() {
        return createSk11UnitDto(dto -> {
        });
    }

    public static Sk11UnitDto createSk11UnitDto(Consumer<Sk11UnitDto> consumer) {
        Sk11UnitDto sk11UnitDto = new Sk11UnitDto();
        sk11UnitDto.setUid(UUID.randomUUID());
        sk11UnitDto.setName("name");
        sk11UnitDto.setObjectType("type");
        sk11UnitDto.setTelemetries(Collections.singletonList(createSk11TelemetryDto()));
        sk11UnitDto.setChannels(Collections.singletonList(createSk11ChannelDto()));
        //тут добавим каналы и телеметрию каналов
        consumer.accept(sk11UnitDto);
        return sk11UnitDto;
    }

    public static Sk11TelemetryDto createSk11TelemetryDto() {
        return createSk11TelemetryDto(dto -> {
        });
    }

    public static Sk11TelemetryDto createSk11TelemetryDto(Consumer<Sk11TelemetryDto> consumer) {
        Sk11TelemetryDto sk11TelemetryDto = new Sk11TelemetryDto();
        sk11TelemetryDto.setUid(UUID.randomUUID());
        sk11TelemetryDto.setName("name");
        sk11TelemetryDto.setObjectType("type");
        sk11TelemetryDto.setParentType("parentType");
        sk11TelemetryDto.setValuesSet(createSk11ValuesSetDto());
        consumer.accept(sk11TelemetryDto);
        return sk11TelemetryDto;
    }

    public static Sk11ValuesSetDto createSk11ValuesSetDto() {
        return createSk11ValuesSetDto(dto -> {
        });
    }

    public static Sk11ValuesSetDto createSk11ValuesSetDto(Consumer<Sk11ValuesSetDto> consumer) {
        Sk11ValuesSetDto sk11ValuesSetDto = new Sk11ValuesSetDto();
        sk11ValuesSetDto.setUid(UUID.randomUUID());
        sk11ValuesSetDto.setName("name");
        sk11ValuesSetDto.setValues(Collections.singletonList(createSk11ValuesSetValueDto()));
        consumer.accept(sk11ValuesSetDto);
        return sk11ValuesSetDto;
    }

    public static Sk11ValuesSetValueDto createSk11ValuesSetValueDto() {
        return createSk11ValuesSetValueDto(dto -> {
        });
    }

    public static Sk11ValuesSetValueDto createSk11ValuesSetValueDto(Consumer<Sk11ValuesSetValueDto> consumer) {
        Sk11ValuesSetValueDto sk11ValuesSetValueDto = new Sk11ValuesSetValueDto();
        sk11ValuesSetValueDto.setUid(UUID.randomUUID());
        sk11ValuesSetValueDto.setName("name");
        sk11ValuesSetValueDto.setValue(1);
        consumer.accept(sk11ValuesSetValueDto);
        return sk11ValuesSetValueDto;
    }

    public static Sk11ChannelDto createSk11ChannelDto() {
        return createSk11ChannelDto(dto -> {
        });
    }

    public static Sk11ChannelDto createSk11ChannelDto(Consumer<Sk11ChannelDto> consumer) {
        Sk11ChannelDto sk11ChannelDto = new Sk11ChannelDto();
        sk11ChannelDto.setUid(UUID.randomUUID());
        sk11ChannelDto.setName("name");
        sk11ChannelDto.setPriority(1);
        sk11ChannelDto.setObjectType("type");
        sk11ChannelDto.setTelemetries(Collections.singletonList(createSk11TelemetryDto()));
        consumer.accept(sk11ChannelDto);
        return sk11ChannelDto;
    }

}
