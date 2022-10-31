package com.microservice.transaction.domain.dto;

import lombok.Data;

import java.util.Date;
@Data
public class TransactionDto {
    private String id;
    private String idCustomerProductSource;
    private String idCustomerProductDestiny;
    private String idTransactionType;
    private Float amount;
    private Date date;
    private String description;
    private TransactionTypeDto transactionType;
    private CustomerProductDto customerProductSource;
    private Date createAt;
    private Date updateAt;
    private String status;

}
