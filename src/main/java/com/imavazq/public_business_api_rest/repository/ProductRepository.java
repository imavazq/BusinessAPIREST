package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> { //<Entidad, tipo_PK>
}
