package com.microservice.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SignatoryDto {
    private String name;
    private String documentType;
    private String documentNumber;
}
