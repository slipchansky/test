package com.upsic.kkc.sk11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import com.upsic.kkc.sk11.enums.PowerGenType;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PowerGenTelemetryGuidDto {

    private UUID uid; //Uid ГОУ

    private UUID telemetryUid; //Uid телеметрии для ГОУ

    private PowerGenType measurementType;

}
