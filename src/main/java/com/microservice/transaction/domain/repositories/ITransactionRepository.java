package com.microservice.transaction.domain.repositories;

import com.microservice.transaction.domain.entities.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransactionRepository extends ReactiveMongoRepository<TransactionEntity, String> {
}
