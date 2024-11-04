package com.imavazq.public_business_api_rest.service.impl;

import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.repository.ProductTypeRepository;
import com.imavazq.public_business_api_rest.service.IProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service//Especificamos que se trata de una clase Service (Bean)
public class ProductTypeServiceImpl implements IProductTypeService {
    private ProductTypeRepository productTypeRepository;

    //Inyecto bean repository
    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductTypeEntity save(ProductTypeEntity productTypeEntity) {
        return productTypeRepository.save(productTypeEntity);
    }

    @Override
    public List<ProductTypeEntity> findAll() {
        return StreamSupport.stream(productTypeRepository //paso a Stream para poder pasar Iterable a Stream
                                .findAll()
                                .spliterator(), //convierto el Iterable<ProductTypeEntity> en Spliterator<ProductTypeEntity>, para poder crear el Stream
                        false)//stream secuencial
                .collect(Collectors.toList()); //convierto a lista
    }

    @Override
    public Optional<ProductTypeEntity> findOne(Long id) {
        return productTypeRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return productTypeRepository.existsById(id);
    }

    @Override
    public ProductTypeEntity partialUpdate(Long id, ProductTypeEntity productTypeEntity) {
        productTypeEntity.setId(id); //aseguro nuevamente que id sea el pasado como path variable

        return productTypeRepository.findById(id).map(existingProductType -> { //Si encuentra existingProductType (productType resultado)
            //Si nueva description de productType a actualizar no es null -> se lo asigno al productType recuperado
            Optional.ofNullable(productTypeEntity.getDescription()).ifPresent(existingProductType::setDescription);

            //actualizo producType recuperado con nueva desc
            return productTypeRepository.save(existingProductType);
        }).orElseThrow(() -> new RuntimeException("El tipo de producto no existe.")); //Si no se encuentra en la BD
    }

    @Override
    public void delete(Long id) {
        productTypeRepository.deleteById(id);
    }
}