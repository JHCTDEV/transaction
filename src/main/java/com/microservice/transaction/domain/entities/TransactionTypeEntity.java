package com.microservice.transaction.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "transactionType")
@Data
public class TransactionTypeEntity {
    @Id
    private String id;
    private String code;
    private String description;
    private Date createAt;
    private Date updateAt;
    private String status;
}
