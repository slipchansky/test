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
public class Sk11ChannelDto {

    private UUID uid;

    private String objectType;

    private String name; // Название

    private Integer priority;

    private List<Sk11TelemetryDto> telemetries;


}
