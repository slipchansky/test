package com.upsic.kkc.sk11.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class TelemetryValuesWriteDto {

    private String writeType;

    private List<TelemetryValueDto> values;

}
