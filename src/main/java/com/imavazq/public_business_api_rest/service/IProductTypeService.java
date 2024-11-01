package com.imavazq.public_business_api_rest.service;

import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {

    ProductTypeEntity save(ProductTypeEntity productTypeEntity);

    List<ProductTypeEntity> findAll();

    Optional<ProductTypeEntity> findOne(Long id);

    boolean isExists(Long id);

    ProductTypeEntity partialUpdate(Long id, ProductTypeEntity productTypeEntity);

    void delete(Long id);
}
