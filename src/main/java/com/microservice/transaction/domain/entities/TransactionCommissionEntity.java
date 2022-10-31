package com.microservice.transaction.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "transactionCommission")
public class TransactionCommissionEntity {
    @Id
    private  String id;
    private  String idTransaction;
    private Integer amount;
    private String idCommissionType;
    private Date createAt;
    private Date updateAt;
    private  String status;
}
