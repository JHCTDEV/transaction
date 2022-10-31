package com.microservice.transaction.infrastructure.repositories;

import com.microservice.transaction.domain.repositories.ICustomerProductRestRepository;
import com.microservice.transaction.domain.dto.CustomerProductDto;
import com.microservice.transaction.domain.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class CustomerProductRestRepository implements ICustomerProductRestRepository {
    private       String            uriBase = "http://CUSTOMERPRODUCT-SERVICE";
    private final WebClient.Builder builder;
    @Autowired
    public CustomerProductRestRepository(WebClient.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Mono<ResponseDto> getTransactionLimitByTypeAndCustomerProduct(String idCustomerProduct, String idTransactionType) {
        return this.builder.baseUrl(this.uriBase).build().get().uri("/customerProduct/get/{idCustomerProduct}/{idTransactionType}",idCustomerProduct, idTransactionType)
                .retrieve()
                .bodyToMono(ResponseDto.class);
    }

    @Override
    public Mono<ResponseDto> getById(String id) {
        return this.builder.baseUrl(this.uriBase).build().get().uri("/customerProduct/get/{id}",id)
                .retrieve()
                .bodyToMono(ResponseDto.class);

    }

    @Override
    public Mono<ResponseDto> update(CustomerProductDto customerProductDto) {
        return this.builder.baseUrl(this.uriBase).build().put().uri("/customerProduct/save")
                .bodyValue(customerProductDto)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ResponseDto.class);

    }
}
