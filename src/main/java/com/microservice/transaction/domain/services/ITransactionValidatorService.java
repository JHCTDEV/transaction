package com.microservice.transaction.domain.services;

import com.microservice.transaction.domain.dto.*;
import reactor.core.publisher.Mono;

public interface ITransactionValidatorService {
    Mono<ResponseDto> validateCreateTransaction(TransactionDto transactionDto);
    Mono<ResponseDto> validateByTransactionType(TransactionDto transactionDto, CustomerProductDto customerProductDto, TransactionTypeDto transactionTypeDto, ProductDto productDto);
    Mono<ResponseDto> validateByTransactionWithdraw(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto);
    Mono<ResponseDto> validateByTransactionDeposit(TransactionDto transactionDto, CustomerProductDto customerProductDto, ProductDto productDto);
    Mono<ResponseDto> validateByTransactionCardConsumption(TransactionDto transactionDto, CustomerProductDto customerProductDto,ProductDto productDto);
    Mono<ResponseDto> validateByTransactionCardPayment(TransactionDto transactionDto, CustomerProductDto customerProductDto,ProductDto productDto);
    Mono<ResponseDto> validateByTransactionSameBankTransferOwner(TransactionDto transactionDto, CustomerProductDto customerProductDto,ProductDto productDto);
    Mono<ResponseDto> validateByTransactionSameBankTransferThird(TransactionDto transactionDto, CustomerProductDto customerProductDto,ProductDto productDto);
}
