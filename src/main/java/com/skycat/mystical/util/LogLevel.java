package com.skycat.mystical.util;

public enum LogLevel {
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
