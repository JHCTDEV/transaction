package com.microservice.transaction.domain.services;

import com.microservice.transaction.domain.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface ITransactionExceptionService {
    Mono<ResponseDto> convertToDto(Throwable exception);
}
