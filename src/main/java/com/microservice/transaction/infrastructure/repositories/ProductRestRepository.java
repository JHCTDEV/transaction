package com.microservice.transaction.infrastructure.repositories;

import com.microservice.transaction.domain.repositories.IProductRestRepository;
import com.microservice.transaction.domain.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class ProductRestRepository implements IProductRestRepository {
    private       String            uriBase = "http://PRODUCT-SERVICE";
    private final WebClient.Builder builder;
    @Autowired
    public ProductRestRepository(WebClient.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Mono<ResponseDto> getAll() {
        return this.builder.baseUrl(this.uriBase).build().get().uri("/product/list")
                .retrieve()
                .bodyToMono(ResponseDto.class);
    }

    @Override
    public Mono<ResponseDto> getById(String id) {
        return this.builder.baseUrl(this.uriBase).build().get().uri("/product/get/{id}",id)
                .retrieve()
                .bodyToMono(ResponseDto.class);

    }
    @Override
    public Mono<ResponseDto> getTypeById(String id) {
        return this.builder.baseUrl(this.uriBase).build().get().uri("/productType/getByProduct/{id}",id)
                .retrieve()
                .bodyToMono(ResponseDto.class);
    }
}
