package com.microservice.transaction.domain.services;

import com.microservice.transaction.domain.dto.ResponseDto;
import com.microservice.transaction.domain.dto.TransactionDto;
import reactor.core.publisher.Mono;

public interface ITransactionService {
    Mono<ResponseDto> findAll();
    Mono<ResponseDto> create(TransactionDto transactionDto);
    Mono<ResponseDto> update(TransactionDto transactionDto);
    Mono<Void> delete(String id);
    Mono<ResponseDto> findById(String id);
    Mono<ResponseDto> updateBalanceCustomerProduct(ResponseDto responseDto);
}
