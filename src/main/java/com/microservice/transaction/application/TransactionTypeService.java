package com.microservice.transaction.application;

import com.microservice.transaction.domain.entities.TransactionTypeEntity;
import com.microservice.transaction.domain.repositories.ITransactionTypeRepository;
import com.microservice.transaction.domain.services.ITransactionExceptionService;
import com.microservice.transaction.domain.services.ITransactionTypeService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.domain.dto.ResponseDto;
import com.microservice.transaction.domain.dto.TransactionTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionTypeService implements ITransactionTypeService {
    @Autowired
    private ITransactionTypeRepository transactionTypeRepository;
    @Autowired
    private IModelMapper modelMapper;
    @Autowired
    private ITransactionExceptionService transactionExceptionService;

    @Override
    public Mono<ResponseDto> findAll() {
        return this.transactionTypeRepository.findAll().collectList().flatMap(listTransactionTypeEntity -> {
            List<TransactionTypeDto> listTransactionTypeDto = listTransactionTypeEntity.stream().map(transactionTypeEntity -> (TransactionTypeDto) this.modelMapper.convert(transactionTypeEntity, TransactionTypeDto.class)).collect(Collectors.toList());
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(listTransactionTypeDto);
            return Mono.just(responseDto);
        });
    }

    @Override
    public Mono<ResponseDto> update(TransactionTypeDto transactionTypeDto) {
        return this.transactionTypeRepository.findById(transactionTypeDto.getId()).flatMap(transactionTypeEntity -> {
            if (transactionTypeEntity.getId() == null)
                return Mono.error(new Exception("transaction type does not exist"));
            return this.transactionTypeRepository.save(transactionTypeEntity).flatMap(entity -> {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setSuccess(true);
                responseDto.setData(this.modelMapper.convert(entity, TransactionTypeDto.class));
                return Mono.just(responseDto);
            });

        }).onErrorResume(transactionExceptionService::convertToDto);
    }

    @Override
    public Mono<ResponseDto> create(TransactionTypeDto transactionTypeDto) {
        TransactionTypeEntity transactionTypeEntity = (TransactionTypeEntity) this.modelMapper.convert(transactionTypeDto, TransactionTypeEntity.class);
        transactionTypeEntity.setCreateAt(new Date());
        transactionTypeEntity.setStatus("1");
        return this.transactionTypeRepository.save(transactionTypeEntity).flatMap(entity -> {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(this.modelMapper.convert(entity, TransactionTypeDto.class));
            return Mono.just(responseDto);
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.transactionTypeRepository.deleteById(id);
    }

    @Override
    public Mono<ResponseDto> findById(String id) {
        return this.transactionTypeRepository.findById(id).flatMap(transactionTypeEntity -> {
            TransactionTypeDto transactionTypeDto = (TransactionTypeDto) this.modelMapper.convert(transactionTypeEntity, TransactionTypeDto.class);
            ResponseDto responseDto = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(transactionTypeDto);
            return Mono.just(responseDto);
        }).defaultIfEmpty(new ResponseDto());
    }
}
