package com.microservice.transaction.domain.repositories;

import com.microservice.transaction.domain.entities.TransactionTypeEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransactionTypeRepository extends ReactiveMongoRepository<TransactionTypeEntity, String> {
}
