package com.upsic.kkc.sk11.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class TelemetryValueDto {

    private UUID uid;

    /* фактическое время значения */
    private Instant timeStamp;

    /* время создания записи по значению */
    private Instant timeStamp2;

    @JsonProperty("qCode")
    private Long qCode;

    private Double value;

}
