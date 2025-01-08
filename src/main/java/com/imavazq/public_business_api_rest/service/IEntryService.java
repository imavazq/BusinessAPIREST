package com.imavazq.public_business_api_rest.service;

import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IEntryService {
    EntryEntity save(EntryEntity entryEntity);

    List<EntryEntity> findAll();

    Optional<EntryEntity> findOne(Long id);

    boolean isExists(Long id);

    EntryEntity partialUpdate(Long id, EntryEntity entryEntity);

    void delete(Long id);
}
