package com.upsic.kkc.sk11.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TelemetryWriteType {
    FORCED_TO_ARCHIVE("forcedToArchive"),
    MANUAL_WRITE("manualWrite");

    @JsonValue
    final private String sk11Value;

    TelemetryWriteType(String sk11Value) {
        this.sk11Value = sk11Value;
    }

    public String getSk11Value() {
        return sk11Value;
    }

}