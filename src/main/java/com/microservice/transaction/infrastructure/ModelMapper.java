package com.microservice.transaction.infrastructure;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class ModelMapper implements IModelMapper {
    @Override
    public Object convert(Object input, Type output) {
        org.modelmapper.ModelMapper  modelMapper = new org.modelmapper.ModelMapper();
        return modelMapper.map(input,output);
    }
}
