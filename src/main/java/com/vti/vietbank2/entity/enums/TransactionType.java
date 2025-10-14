package com.vti.vietbank2.entity.enums;

public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT;

    public static TransactionType fromString(String type) {
        return TransactionType.valueOf(type.toUpperCase());
    }
}
