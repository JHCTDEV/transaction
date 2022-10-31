package com.microservice.transaction.application;

import com.microservice.transaction.domain.entities.TransactionEntity;
import com.microservice.transaction.domain.entities.TransactionTypeEntity;
import com.microservice.transaction.domain.enums.TransactionTypeEnum;
import com.microservice.transaction.domain.repositories.ICustomerProductRestRepository;
import com.microservice.transaction.domain.repositories.ITransactionRepository;
import com.microservice.transaction.domain.repositories.ITransactionTypeRepository;
import com.microservice.transaction.domain.services.ITransactionExceptionService;
import com.microservice.transaction.domain.services.ITransactionService;
import com.microservice.transaction.domain.services.ITransactionValidatorService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.domain.dto.CustomerProductDto;
import com.microservice.transaction.domain.dto.ResponseDto;
import com.microservice.transaction.domain.dto.TransactionDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TransactionService implements ITransactionService {
    private final ITransactionRepository         transactionRepository;
    private final ICustomerProductRestRepository customerProductRestRepository;
    private final IModelMapper                   modelMapper;
    private final ITransactionExceptionService   transactionExceptionService;
    private final ITransactionValidatorService   transactionValidatorService;
    private final ITransactionTypeRepository     transactionTypeRepository;

    @Autowired
    public TransactionService(ITransactionRepository transactionRepository, ICustomerProductRestRepository customerProductRestRepository, IModelMapper modelMapper, ITransactionExceptionService transactionExceptionService, ITransactionValidatorService transactionValidatorService, ITransactionTypeRepository transactionTypeRepository) {
        this.transactionRepository         = transactionRepository;
        this.customerProductRestRepository = customerProductRestRepository;
        this.modelMapper                   = modelMapper;
        this.transactionExceptionService   = transactionExceptionService;
        this.transactionValidatorService   = transactionValidatorService;
        this.transactionTypeRepository     = transactionTypeRepository;
    }

    @Override
    public Mono<ResponseDto> findAll() {
        return this.transactionRepository.findAll().collectList().flatMap(listTransactionEntity -> {
            List<TransactionDto> listTransactionDto = listTransactionEntity.stream().map(transactionEntity -> (TransactionDto) this.modelMapper.convert(transactionEntity, TransactionDto.class)).collect(Collectors.toList());
            ResponseDto          responseDto        = new ResponseDto();
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
        Function<ResponseDto, Mono<ResponseDto>> saveTransaction = responseDto -> {
            if(!responseDto.isSuccess())
                return Mono.just(responseDto);
            return this.transactionRepository.save(transactionEntity).flatMap(entity -> {
                ResponseDto outResponseDto = new ResponseDto();
                outResponseDto.setSuccess(true);
                outResponseDto.setData(this.modelMapper.convert(entity, TransactionDto.class));
                return Mono.just(outResponseDto);
            });
        };
        return this.transactionValidatorService.validateCreateTransaction(transactionDto)
                .flatMap(saveTransaction)
                .flatMap(this::updateBalanceCustomerProduct)
                .onErrorResume(this.transactionExceptionService::convertToDto);
    }

    @Override
    public Mono<ResponseDto> updateBalanceCustomerProduct(ResponseDto responseDto) {
        if (!responseDto.isSuccess()) {
            log.error("failed get data customer product" + responseDto.toString());
            return Mono.just(responseDto);
        }
        TransactionDto transactionDto = (TransactionDto) this.modelMapper.convert(responseDto.getData(), TransactionDto.class);
        Function<ResponseDto, Mono<ResponseDto>> operationSuccess = responseSaveCustomerProductDto -> {
            if (!responseSaveCustomerProductDto.isSuccess()) {
                log.info(responseSaveCustomerProductDto.toString());
                return Mono.just(responseSaveCustomerProductDto);
            }
            ResponseDto responseSuccessDto = new ResponseDto();
            responseSuccessDto.setMessage("Operation performed correctly");
            responseSuccessDto.setSuccess(true);
            return Mono.just(responseSuccessDto);
        };
        Function<TransactionTypeEntity, Mono<ResponseDto>> updateBalance = transactionTypeEntity -> {
            return this.customerProductRestRepository.getById(transactionDto.getIdCustomerProductSource()).flatMap(responseCustomerProductDto -> {
                CustomerProductDto customerProductDto             = (CustomerProductDto) this.modelMapper.convert(responseCustomerProductDto.getData(), CustomerProductDto.class);
                Set<String>        transactionTypeIncreaseBalance = new HashSet<>();
                transactionTypeIncreaseBalance.add(TransactionTypeEnum.DEPOSIT.getValue());
                transactionTypeIncreaseBalance.add(TransactionTypeEnum.CARD_PAYMENT.getValue());
                Float newBalance = transactionTypeIncreaseBalance.contains(transactionTypeEntity.getCode()) ? customerProductDto.getBalance() + transactionDto.getAmount() : customerProductDto.getBalance() - transactionDto.getAmount();
                customerProductDto.setBalance(newBalance);
                return this.customerProductRestRepository.update(customerProductDto).flatMap(operationSuccess);
            });
        };
        return this.transactionTypeRepository.findById(transactionDto.getIdTransactionType()).flatMap(updateBalance);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.transactionRepository.deleteById(id);
    }

    @Override
    public Mono<ResponseDto> findById(String id) {
        return this.transactionRepository.findById(id).flatMap(transactionEntity -> {
            TransactionDto transactionDto = (TransactionDto) this.modelMapper.convert(transactionEntity, TransactionDto.class);
            ResponseDto    responseDto    = new ResponseDto();
            responseDto.setSuccess(true);
            responseDto.setData(transactionDto);
            return Mono.just(responseDto);
        }).defaultIfEmpty(new ResponseDto());
    }
}
