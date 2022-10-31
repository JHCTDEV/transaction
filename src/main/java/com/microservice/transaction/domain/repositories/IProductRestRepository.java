package com.microservice.transaction.domain.repositories;


import com.microservice.transaction.domain.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface IProductRestRepository {
    Mono<ResponseDto> getAll();
    Mono<ResponseDto> getById(String id);
    Mono<ResponseDto> getTypeById(String id);
}
