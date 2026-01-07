package com.vti.vietbank.entity.enums;

public enum DepositStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;
    public static DepositStatus fromString(String status) {
        return DepositStatus.valueOf(status.toUpperCase());
    }

}
