package com.upsic.kkc.sk11.enums;

import java.util.stream.Stream;

public enum GouType {
    GOU("ГОУ"),
    RGE("РГЕ"),
    NBLOCK("Блоки"),
    SECH("Сечение"),
    VETV("Ветвь");

    private final String rusName;

    GouType(String rusName) {
        this.rusName = rusName;
    }

    public String getRusName() {
        return rusName;
    }

}
