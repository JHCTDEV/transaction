package com.microservice.transaction.application;

import com.microservice.transaction.domain.ITransactionRepository;
import com.microservice.transaction.domain.ITransactionService;
import com.microservice.transaction.domain.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private ITransactionRepository transactionRepository;
    @Override
    public Flux<TransactionEntity> findAll() {
        return this.transactionRepository.findAll();
    }

    @Override
    public Mono<TransactionEntity> save(TransactionEntity transaction) {
        return this.transactionRepository.save(transaction);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.transactionRepository.deleteById(id);
    }

    @Override
    public Mono<TransactionEntity> findById(String id) {
        return this.transactionRepository.findById(id);
    }
}
