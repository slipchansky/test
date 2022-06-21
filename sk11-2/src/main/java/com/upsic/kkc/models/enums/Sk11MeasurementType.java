package com.upsic.kkc.models.enums;

public enum Sk11MeasurementType {
    ANALOG("Analog", "Аналог"),
    DISCRETE("Discrete", "Дискрет"),
    OTHER("Other", "Другое");

    final private String sk11Value;
    final private String name;

    Sk11MeasurementType(String sk11Value, String name) {
        this.sk11Value = sk11Value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSk11Value() {
        return sk11Value;
    }

}