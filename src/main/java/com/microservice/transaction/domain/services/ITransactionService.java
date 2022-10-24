package com.microservice.transaction.domain.services;

import com.microservice.transaction.domain.entities.TransactionEntity;
import com.microservice.transaction.infrastructure.dto.ResponseDto;
import com.microservice.transaction.infrastructure.dto.TransactionDto;
import com.microservice.transaction.infrastructure.dto.TransactionTypeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {
    Mono<ResponseDto> findAll();
    Mono<ResponseDto> create(TransactionDto transactionDto);
    Mono<ResponseDto> update(TransactionDto transactionDto);
    Mono<Void> delete(String id);
    Mono<ResponseDto> findById(String id);
}
