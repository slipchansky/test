package com.upsic.kkc.sk11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import com.upsic.kkc.sk11.enums.QReserveType;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class QReserveTelemetryGuidDto {

    private UUID uid;

    private UUID telemetryUid;

    private QReserveType measurementType;

}
