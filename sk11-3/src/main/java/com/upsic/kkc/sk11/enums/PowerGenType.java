package com.upsic.kkc.sk11.enums;

public enum PowerGenType {
    PGEN("Генерация активной мощности"),
    QGEN("Генерация реактивной мощности");

    final private String name;

    PowerGenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
