package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductTypeRepository extends CrudRepository<ProductTypeEntity, Long> {
}
