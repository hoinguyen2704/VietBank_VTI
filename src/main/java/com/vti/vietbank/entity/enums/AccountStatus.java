package com.vti.vietbank.entity.enums;

public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    CLOSED;

    public static AccountStatus fromString(String status) {
        return AccountStatus.valueOf(status.toUpperCase());
    }
}

