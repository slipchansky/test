package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11FullMeasurementValueDto extends Sk11NodeDto {

    private Sk11NodeDto category;

    private Sk11NodeDto energyObject;

    private Sk11UnitDto unit;

    private String measurementType;

}
