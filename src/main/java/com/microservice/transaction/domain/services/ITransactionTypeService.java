package com.microservice.transaction.domain.services;

import com.microservice.transaction.infrastructure.dto.ResponseDto;
import com.microservice.transaction.infrastructure.dto.TransactionTypeDto;
import reactor.core.publisher.Mono;

public interface ITransactionTypeService {
    Mono<ResponseDto> findAll();
    Mono<ResponseDto> create(TransactionTypeDto transactionTypeDto);
    Mono<ResponseDto> update(TransactionTypeDto transactionTypeDto);
    Mono<Void> delete(String id);
    Mono<ResponseDto> findById(String id);
}
