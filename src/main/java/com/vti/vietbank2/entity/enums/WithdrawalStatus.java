package com.vti.vietbank2.entity.enums;

public enum WithdrawalStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED;
    public static WithdrawalStatus fromString(String status) {
        return WithdrawalStatus.valueOf(status.toUpperCase());
    }

}
