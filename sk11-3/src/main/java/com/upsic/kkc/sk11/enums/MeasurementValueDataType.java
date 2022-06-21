package com.upsic.kkc.sk11.enums;

public enum MeasurementValueDataType {
    NUMERIC("Числовой", "numeric"),
    STRING("Строковый", "string");

    final private String name;
    final private String path;

    MeasurementValueDataType(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
