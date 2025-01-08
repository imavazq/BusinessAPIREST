package com.imavazq.public_business_api_rest.mapper.impl;

import com.imavazq.public_business_api_rest.domain.dto.EntryDto;
import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EntryMapperImpl implements IMapper<EntryEntity, EntryDto> {
    private ModelMapper modelMapper;

    public EntryMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntryDto mapTo(EntryEntity entryEntity) {
        return modelMapper.map(entryEntity, EntryDto.class);
    }

    @Override
    public EntryEntity mapFrom(EntryDto entryDto) {
        return modelMapper.map(entryDto, EntryEntity.class);
    }
}
