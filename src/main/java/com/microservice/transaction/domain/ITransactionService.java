package com.microservice.transaction.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {
    Flux<TransactionEntity> findAll();
    Mono<TransactionEntity> save(TransactionEntity customer);
    Mono<Void> delete(String id);
    Mono<TransactionEntity> findById(String id);
}
