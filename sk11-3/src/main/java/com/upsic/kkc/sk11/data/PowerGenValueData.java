package com.upsic.kkc.sk11.data;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import com.upsic.kkc.sk11.enums.PowerGenType;

@AllArgsConstructor
@Setter
@Getter
@Builder
@NoArgsConstructor
public class PowerGenValueData {

    private UUID uid;

    private LocalDateTime requestDate;

    private PowerGenType measurementType;

    private Long qCode;

    private Double value;
}
