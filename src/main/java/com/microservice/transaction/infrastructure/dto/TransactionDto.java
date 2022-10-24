package com.microservice.transaction.infrastructure.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
@Data
public class TransactionDto {
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
