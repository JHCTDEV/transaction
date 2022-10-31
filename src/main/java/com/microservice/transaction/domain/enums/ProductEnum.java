package com.microservice.transaction.domain.enums;

public enum ProductEnum {
    SAVINGS_ACCOUNT("SAVINGS_ACCOUNT"),
    CURRENT_ACCOUNT("CURRENT_ACCOUNT"),
    FIXED_TERM_ACCOUNT("FIXED_TERM_ACCOUNT"),
    CREDIT_CARD("CREDIT_CARD"),
    CREDIT_ACCOUNT("CREDIT_ACCOUNT");
    private String value;

    ProductEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
