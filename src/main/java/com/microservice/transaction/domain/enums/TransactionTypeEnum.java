package com.microservice.transaction.domain.enums;

public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW"),
    INTERBANK_TRANSFER("INTERBANK_TRANSFER"),
    SAME_BANK_TRANSFER_OWNER("SAME_BANK_TRANSFER_OWNER"),
    CARD_PAYMENT("CARD_PAYMENT"),
    CARD_CONSUMPTION("CARD_CONSUMPTION"),
    SAME_BANK_TRANSFER_THIRD("SAME_BANK_TRANSFER_THIRD");

    private String value;

    TransactionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
