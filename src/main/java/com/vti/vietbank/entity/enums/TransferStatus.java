package com.vti.vietbank.entity.enums;

public enum TransferStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;

    public static TransferStatus fromString(String status) {
        return TransferStatus.valueOf(status.toUpperCase());
    }
}
