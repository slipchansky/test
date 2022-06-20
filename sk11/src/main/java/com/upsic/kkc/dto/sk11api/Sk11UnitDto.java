package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11UnitDto {

    private UUID uid;

    private String objectType;

    private String name;

    private Integer notTopicalTimeout;

    private Integer notUpdatedTimeout;

    private List<Sk11ChannelDto> channels;

    private List<Sk11TelemetryDto> telemetries;


}
