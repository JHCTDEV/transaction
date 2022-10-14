package com.microservice.transaction.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransactionRepository extends ReactiveMongoRepository<TransactionEntity, String> {
}
