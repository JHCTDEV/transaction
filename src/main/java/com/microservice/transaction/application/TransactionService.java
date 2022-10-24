package com.microservice.transaction.application;

import com.microservice.transaction.domain.entities.TransactionEntity;
import com.microservice.transaction.domain.entities.TransactionTypeEntity;
import com.microservice.transaction.domain.repositories.ITransactionRepository;
import com.microservice.transaction.domain.services.ITransactionExceptionService;
import com.microservice.transaction.domain.services.ITransactionService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.infrastructure.dto.ResponseDto;
import com.microservice.transaction.infrastructure.dto.TransactionDto;
import com.microservice.transaction.infrastructure.dto.TransactionTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private IModelMapper modelMapper;
    @Autowired
    private ITransactionExceptionService transactionExceptionService;

    @Override
    public Mono<ResponseDto> findAll() {
        return this.transactionRepository.findAll().collectList().flatMap(listTransactionEntity -> {
            List<TransactionDto> listTransactionDto = listTransactionEntity.stream().map(transactionEntity -> (TransactionDto) this.modelMapper.convert(transactionEntity, TransactionDto.class)).collect(Collectors.toList());
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(listTransactionDto);
            return Mono.just(responseDto);
        });
    }

    @Override
    public Mono<ResponseDto> update(TransactionDto transactionDto) {
        return this.transactionRepository.findById(transactionDto.getId()).flatMap(transactionEntity -> {
            if (transactionEntity.getId() == null)
                return Mono.error(new Exception("transaction does not exist"));
            return this.transactionRepository.save(transactionEntity).flatMap(entity -> {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setSuccess(true);
                responseDto.setData(this.modelMapper.convert(entity, TransactionDto.class));
                return Mono.just(responseDto);
            });

        }).onErrorResume(transactionExceptionService::convertToDto);
    }

    @Override
    public Mono<ResponseDto> create(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = (TransactionEntity) this.modelMapper.convert(transactionDto, TransactionEntity.class);
        transactionEntity.setCreateAt(new Date());
        transactionEntity.setStatus("1");
        return this.transactionRepository.save(transactionEntity).flatMap(entity -> {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(this.modelMapper.convert(entity, TransactionDto.class));
            return Mono.just(responseDto);
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.transactionRepository.deleteById(id);
    }

    @Override
    public Mono<ResponseDto> findById(String id) {
        return this.transactionRepository.findById(id).flatMap(transactionEntity -> {
            TransactionDto transactionDto = (TransactionDto) this.modelMapper.convert(transactionEntity, TransactionDto.class);
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(transactionDto);
            return Mono.just(responseDto);
        }).defaultIfEmpty(new ResponseDto());
    }
}
