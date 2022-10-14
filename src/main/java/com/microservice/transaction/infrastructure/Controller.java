package com.microservice.transaction.infrastructure;

import com.microservice.transaction.domain.ITransactionService;
import com.microservice.transaction.domain.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("transaction")
public class Controller {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping(value = "list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionEntity> findAll(){
        return this.transactionService.findAll();
    }

    @GetMapping(value = "get/{id}")
    Mono<TransactionEntity> findById(@PathVariable("id") String id){
        return this.transactionService.findById(id);
    }

    @DeleteMapping("delete/{id}")
    public Mono<Void> delete(@PathVariable("id") String id){
        return this.transactionService.delete(id);
    }

    @PostMapping("save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionEntity> save(@RequestBody TransactionEntity customer){
        return this.transactionService.save(customer);

    }
}
