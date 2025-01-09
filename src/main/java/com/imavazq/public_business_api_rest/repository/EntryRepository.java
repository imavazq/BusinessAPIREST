package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends CrudRepository<EntryEntity, Long> {
}
