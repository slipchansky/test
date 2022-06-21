package com.upsic.kkc.sk11.enums;

public enum QReserveType {
    LOAD_RESERVE("Резерв на загрузку по реактивной мощности"),
    UNLOAD_RESERVE("Резерв на разгрузку по реактивной мощности");

    final private String name;

    QReserveType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}