package com.upsic.kkc.sk11.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class MeasurementValuesDto {

    private List<UUID> uids;

    @JsonProperty("timeStamp")
    private Instant timestamp;
}
