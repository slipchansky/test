package com.upsic.kkc.sk11.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class TelemetryValueIntervalDto {

    @JsonProperty("@nextLink")
    private String nextLink;

    private List<TelemetryValueDto> value;

}
