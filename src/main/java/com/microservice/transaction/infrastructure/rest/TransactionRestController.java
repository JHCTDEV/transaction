package com.microservice.transaction.infrastructure.rest;

import com.microservice.transaction.domain.services.ITransactionService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.infrastructure.dto.ResponseDto;
import com.microservice.transaction.infrastructure.dto.TransactionDto;
import com.microservice.transaction.infrastructure.dto.TransactionTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("transaction")
public class TransactionRestController {

    @Autowired
    private ITransactionService transactionService;
    @Autowired
    private IModelMapper modelMapper;
    @PostMapping("save")
    public Mono<ResponseDto> create(@RequestBody TransactionDto transactionDto){
        return this.transactionService.create(transactionDto);
    }
    @PutMapping("save")
    public Mono<ResponseDto> update(@RequestBody TransactionDto transactionDto){
        return this.transactionService.update(transactionDto);
    }
    @GetMapping("list")
    public Mono<ResponseDto> findAll(){
        return this.transactionService.findAll();
    }
    @GetMapping("get/{id}")
    public Mono<ResponseDto> getById(@PathVariable("id") String id){
        return this.transactionService.findById(id);
    }

}
