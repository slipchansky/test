package com.upsic.kkc.sk11.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class TelemetryValuesDeleteDto {

    private List<TelemetryDeleteDto> selectors;

}
