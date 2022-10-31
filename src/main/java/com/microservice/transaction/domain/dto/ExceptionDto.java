package com.microservice.transaction.domain.dto;

import lombok.Data;

@Data
public class ExceptionDto {
    private Integer code;
    private String message;
}
