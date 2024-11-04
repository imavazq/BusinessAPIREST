package com.imavazq.public_business_api_rest.service;

import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductEntity save(ProductEntity productEntity);

    List<ProductEntity> findAll();

    Optional<ProductEntity> findOne(Long id);

    boolean isExists(Long id);

    ProductEntity partialUpdate(Long id, ProductEntity productEntity);

    void delete(Long id);
}
