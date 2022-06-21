package com.upsic.kkc.sk11.data;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

import com.upsic.kkc.sk11.enums.QReserveType;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class QReserveValueData {

    private UUID uid;

    private QReserveType measurementType;

    private Instant requestDate;

    private Double value;
}
