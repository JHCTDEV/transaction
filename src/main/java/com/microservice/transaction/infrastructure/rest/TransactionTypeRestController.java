package com.microservice.transaction.infrastructure.rest;

import com.microservice.transaction.domain.services.ITransactionTypeService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.infrastructure.dto.ResponseDto;
import com.microservice.transaction.infrastructure.dto.TransactionTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class TransactionTypeRestController {
    @Autowired
    private ITransactionTypeService transactionTypeService;
    @Autowired
    private IModelMapper modelMapper;
    @PostMapping("save")
    public Mono<ResponseDto> create(@RequestBody TransactionTypeDto customerTypeDto){
        return this.transactionTypeService.create(customerTypeDto);
    }
    @PutMapping("save")
    public Mono<ResponseDto> update(@RequestBody TransactionTypeDto customerTypeDto){
        return this.transactionTypeService.update(customerTypeDto);
    }
    @GetMapping("list")
    public Mono<ResponseDto> findAll(){
        return this.transactionTypeService.findAll();
    }
    @GetMapping("get/{id}")
    public Mono<ResponseDto> getById(@PathVariable("id") String id){
        return this.transactionTypeService.findById(id);
    }
}
