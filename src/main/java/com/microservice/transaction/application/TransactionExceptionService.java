package com.microservice.transaction.application;

import com.microservice.transaction.domain.services.ITransactionExceptionService;
import com.microservice.transaction.domain.dto.ExceptionDto;
import com.microservice.transaction.domain.dto.ResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionExceptionService implements ITransactionExceptionService {
    @Override
    public Mono<ResponseDto> convertToDto(Throwable exception) {
        System.out.println(exception.toString());
        ResponseDto responseDto = new ResponseDto();
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(exception.getMessage());
        responseDto.setSuccess(false);
        responseDto.setError(exceptionDto);
        return Mono.just(responseDto);
    }
}
