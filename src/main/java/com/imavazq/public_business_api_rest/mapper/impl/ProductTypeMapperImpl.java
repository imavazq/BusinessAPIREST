package com.imavazq.public_business_api_rest.mapper.impl;

import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component //Especifico que es Bean
public class ProductTypeMapperImpl implements IMapper<ProductTypeEntity, ProductTypeDto> {
    private ModelMapper modelMapper;

    //@Autowired
    public ProductTypeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductTypeDto mapTo(ProductTypeEntity productTypeEntity) {
        return modelMapper.map(productTypeEntity, ProductTypeDto.class);
    }

    @Override
    public ProductTypeEntity mapFrom(ProductTypeDto productTypeDto) {
        return modelMapper.map(productTypeDto, ProductTypeEntity.class);
    }
}
