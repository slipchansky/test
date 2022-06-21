package com.upsic.kkc.sk11.enums;

import java.util.stream.Stream;

public enum AdderType {
    TER("Территория"),
    GEN("Генерация"),
    RGE("РГЕ"),
    POTR("Потребление"),
    SALDO("Сальдо");

    private final String rusName;

    AdderType(String rusName) {
        this.rusName = rusName;
    }

    public String getRusName() {
        return rusName;
    }

}
