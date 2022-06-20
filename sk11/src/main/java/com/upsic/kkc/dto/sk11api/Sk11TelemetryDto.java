package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11TelemetryDto {

    private UUID uid;

    private String objectType;

    private String name;

    private String parentType;

    private Sk11ValuesSetDto valuesSet;

}
