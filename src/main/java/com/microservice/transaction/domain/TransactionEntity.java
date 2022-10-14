package com.microservice.transaction.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
@Data
public class TransactionEntity {
    @Id
    private String id;
    private String idCustomerProduct;
    private String transactionType;
    private String amount;
    private String date;
    private String description;
    private String status;
}
