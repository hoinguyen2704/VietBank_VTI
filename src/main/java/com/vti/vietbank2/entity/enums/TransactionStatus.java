package com.vti.vietbank2.entity.enums;

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;

    public static TransactionStatus fromString(String status) {
        return TransactionStatus.valueOf(status.toUpperCase());
    }
}
