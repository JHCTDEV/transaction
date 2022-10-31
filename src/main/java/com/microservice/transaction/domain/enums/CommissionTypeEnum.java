package com.microservice.transaction.domain.enums;

public enum CommissionTypeEnum {
    CASH_WITHDRAWAL("CASH_WITHDRAWAL"),
    CASH_DEPOSIT("CASH_DEPOSIT");

    private String value;

    CommissionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
