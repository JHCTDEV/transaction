package com.microservice.transaction.infrastructure.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionTypeDto {
    private String id;
    private String code;
    private String description;
    private Date createAt;
    private Date updateAt;
    private String status;
}
