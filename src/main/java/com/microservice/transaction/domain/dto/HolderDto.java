package com.microservice.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class HolderDto {
    private String name;
    private String documentType;
    private String documentNumber;
}
