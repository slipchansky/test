package com.upsic.kkc.sk11.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ConverterTime {

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, OffsetDateTime.now().getOffset());
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(OffsetDateTime.now().getOffset());
    }
}
