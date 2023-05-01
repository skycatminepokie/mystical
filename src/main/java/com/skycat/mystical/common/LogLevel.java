package com.skycat.mystical.common;

public enum LogLevel {
    // CREDIT: https://www.baeldung.com/java-enum-values
    OFF(0),
    INFO(1),
    DEBUG(2),
    WARN(3),
    ERROR(4);

    public final int intValue;

    LogLevel(int intValue) {
        this.intValue = intValue;
    }
}
