package com.microservice.transaction.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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
    private Date createAt;
    private Date updateAt;
    private String status;
}
