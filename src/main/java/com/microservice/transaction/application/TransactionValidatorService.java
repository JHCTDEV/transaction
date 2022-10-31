package com.microservice.transaction.application;

import com.microservice.transaction.domain.dto.*;
import com.microservice.transaction.domain.enums.ProductEnum;
import com.microservice.transaction.domain.enums.TransactionTypeEnum;
import com.microservice.transaction.domain.repositories.IProductRestRepository;
import com.microservice.transaction.domain.repositories.ITransactionTypeRepository;
import com.microservice.transaction.domain.services.ITransactionExceptionService;
import com.microservice.transaction.domain.services.ITransactionValidatorService;
import com.microservice.transaction.infrastructure.IModelMapper;
import com.microservice.transaction.infrastructure.repositories.CustomerProductRestRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TransactionValidatorService implements ITransactionValidatorService {
    private final IModelMapper                  modelMapper;
    private final ITransactionExceptionService  transactionExceptionService;
    private final CustomerProductRestRepository customerProductRestRepository;
    private final ITransactionTypeRepository    transactionTypeRepository;
    private final IProductRestRepository        productRestRepository;

    @Autowired
    public TransactionValidatorService(IModelMapper modelMapper, ITransactionExceptionService transactionExceptionService, CustomerProductRestRepository customerProductRestRepository, ITransactionTypeRepository transactionTypeRepository, IProductRestRepository productRestRepository) {
        this.modelMapper                   = modelMapper;
        this.transactionExceptionService   = transactionExceptionService;
        this.customerProductRestRepository = customerProductRestRepository;
        this.transactionTypeRepository     = transactionTypeRepository;
        this.productRestRepository         = productRestRepository;
    }

    @Override
    public Mono<ResponseDto> validateByTransactionWithdraw(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        if (!ProductEnum.SAVINGS_ACCOUNT.getValue().equals(productDto.getCode()) && !ProductEnum.CURRENT_ACCOUNT.getValue().equals(productDto.getCode()))
            return Mono.error(new Exception("withdrawals can only be made from savings and checking accounts"));
        if (customerProductDto.getBalance() < transactionDto.getAmount())
            return Mono.error(new Exception("the account does not have a sufficient balance"));
        ResponseDto outResponseDto = new ResponseDto();
        outResponseDto.setSuccess(true);
        outResponseDto.setData(customerProductDto);
        return Mono.just(outResponseDto);
    }

    @Override
    public Mono<ResponseDto> validateByTransactionDeposit(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        if (!ProductEnum.SAVINGS_ACCOUNT.getValue().equals(productDto.getCode()) && !ProductEnum.CURRENT_ACCOUNT.getValue().equals(productDto.getCode()) && !ProductEnum.FIXED_TERM_ACCOUNT.getValue().equals(productDto.getCode()))
            return Mono.error(new Exception("deposits can only be made from savings, current and fixed-term accounts"));
        ResponseDto outResponseDto = new ResponseDto();
        outResponseDto.setSuccess(true);
        outResponseDto.setData(customerProductDto);
        return Mono.just(outResponseDto);
    }

    @Override
    public Mono<ResponseDto> validateByTransactionCardConsumption(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<ResponseDto> validateByTransactionCardPayment(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<ResponseDto> validateByTransactionSameBankTransferOwner(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<ResponseDto> validateByTransactionSameBankTransferThird(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto) {
        return null;
    }

    @Override
    public Mono<ResponseDto> validateByTransactionType(TransactionDto transactionDto, CustomerProductDto customerProductDto, TransactionTypeDto transactionTypeDto, ProductDto productDto) {
        List<TransactionTypeEnum> transactionTypeEnumList = Arrays.stream(TransactionTypeEnum.values()).filter(transactionTypeEnum -> transactionTypeEnum.getValue().equals(transactionTypeDto.getCode())).collect(Collectors.toList());
        if (transactionTypeEnumList.size() == 0)
            return Mono.error(new Exception("the transaction type not exist"));
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.WITHDRAW)
            return this.validateByTransactionWithdraw(transactionDto, customerProductDto, productDto);
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.DEPOSIT)
            return this.validateByTransactionDeposit(transactionDto, customerProductDto, productDto);
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.CARD_CONSUMPTION)
            return this.validateByTransactionCardConsumption(transactionDto, customerProductDto, productDto);
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.CARD_PAYMENT)
            return this.validateByTransactionCardPayment(transactionDto, customerProductDto, productDto);
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.SAME_BANK_TRANSFER_OWNER)
            return this.validateByTransactionSameBankTransferOwner(transactionDto, customerProductDto, productDto);
        if (transactionTypeEnumList.get(0) == TransactionTypeEnum.SAME_BANK_TRANSFER_THIRD)
            return this.validateByTransactionSameBankTransferThird(transactionDto, customerProductDto, productDto);
        ResponseDto outResponseDto = new ResponseDto();
        outResponseDto.setSuccess(true);
        outResponseDto.setData(customerProductDto);
        return Mono.just(outResponseDto);
    }

    @Override
    public Mono<ResponseDto> validateCreateTransaction(TransactionDto transactionDto) {
        Function<ResponseDto, Mono<ResponseDto>> findProductType = responseDto -> {
            if (!responseDto.isSuccess()) {
                log.error("transaction type with id:" + transactionDto.getTransactionType() + " no found");
                return  Mono.just(responseDto);
            }
            TransactionDto     transactionFullDto = (TransactionDto) this.modelMapper.convert(responseDto.getData(), TransactionDto.class);
            CustomerProductDto customerProductDto = transactionFullDto.getCustomerProductSource();
            TransactionTypeDto transactionTypeDto = transactionFullDto.getTransactionType();
            return this.productRestRepository.getById(customerProductDto.getIdProduct()).flatMap(responseRestProductDto -> {
                ProductDto productDto = (ProductDto) this.modelMapper.convert(responseRestProductDto.getData(), ProductDto.class);
                return this.validateByTransactionType(transactionDto, customerProductDto, transactionTypeDto, productDto);
            });
        };
        Function<ResponseDto, Mono<ResponseDto>> findTransactionType = responseDto -> {
            if (!responseDto.isSuccess()) {
                log.error("customer with id:" + transactionDto.getIdCustomerProductSource() + " no found");
                return  Mono.just(responseDto);
            }
            return this.transactionTypeRepository.findById(transactionDto.getIdTransactionType()).flatMap(transactionTypeEntity -> {
                CustomerProductDto customerProductDto = (CustomerProductDto) this.modelMapper.convert(responseDto.getData(), CustomerProductDto.class);
                TransactionTypeDto transactionTypeDto = (TransactionTypeDto) this.modelMapper.convert(transactionTypeEntity, TransactionTypeDto.class);
                transactionDto.setTransactionType(transactionTypeDto);
                transactionDto.setCustomerProductSource(customerProductDto);
                responseDto.setData(transactionDto);
                return Mono.just(responseDto);
            }).flatMap(findProductType);
        };
        return this.customerProductRestRepository.getById(transactionDto.getIdCustomerProductSource()).flatMap(findTransactionType).onErrorResume(this.transactionExceptionService::convertToDto);
    }
}
