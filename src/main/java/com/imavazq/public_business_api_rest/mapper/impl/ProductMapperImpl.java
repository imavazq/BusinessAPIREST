package com.imavazq.public_business_api_rest.mapper.impl;

import com.imavazq.public_business_api_rest.domain.dto.ProductDto;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements IMapper<ProductEntity, ProductDto> {
    private ModelMapper modelMapper;

    public ProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto mapTo(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDto.class);
    }

    @Override
    public ProductEntity mapFrom(ProductDto productDto) {
        return modelMapper.map(productDto, ProductEntity.class);
    }
}
