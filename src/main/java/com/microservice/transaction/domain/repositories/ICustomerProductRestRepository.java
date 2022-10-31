package com.microservice.transaction.domain.repositories;

import com.microservice.transaction.domain.dto.CustomerProductDto;
import com.microservice.transaction.domain.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface ICustomerProductRestRepository {
    Mono<ResponseDto> getById(String id);
    Mono<ResponseDto> update(CustomerProductDto customerProductDto);
    Mono<ResponseDto> getTransactionLimitByTypeAndCustomerProduct(String idCustomerProduct, String idTransactionType);
}
