package com.imavazq.public_business_api_rest.service.impl;

import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.repository.ProductRepository;
import com.imavazq.public_business_api_rest.repository.ProductTypeRepository;
import com.imavazq.public_business_api_rest.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service//Especificamos que se trata de una clase Service (Bean)
public class ProductServiceImpl implements IProductService {
    private ProductRepository productRepository;
    private ProductTypeRepository productTypeRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductEntity save(ProductEntity productEntity) { return productRepository.save(productEntity); }

    @Override
    public List<ProductEntity> findAll() {
        return StreamSupport.stream(productRepository //paso a Stream para poder pasar Iterable a Stream
                .findAll()
                .spliterator(), //convierto el Iterable<ProductTypeEntity> en Spliterator<ProductTypeEntity>, para poder crear el Stream
                false)//parallel: false (secuencial)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductEntity> findOne(Long id) { return productRepository.findById(id); }

    @Override
    public boolean isExists(Long id) { return productRepository.existsById(id); }

    @Override
    public ProductEntity partialUpdate(Long id, ProductEntity productEntity) {
        productEntity.setId(id); //aseguro que id sea el pasado como path variable

        return productRepository.findById(id).map(existingProduct -> { //Si encuentra existingProduct (product resultado)
            //Si nuevo article de product a actualizar no es null -> se lo asigno al product recuperado
            Optional.ofNullable(productEntity.getArticle()).ifPresent(existingProduct::setArticle);
            Optional.ofNullable(productEntity.getSize()).ifPresent(existingProduct::setSize);
            Optional.ofNullable(productEntity.getDescription()).ifPresent(existingProduct::setDescription);
            Optional.ofNullable(productEntity.getPrice()).ifPresent(existingProduct::setPrice);
            Optional.ofNullable(productEntity.getStockAvailable()).ifPresent(existingProduct::setStockAvailable);
            Optional.ofNullable(productEntity.getAdditionalNotes()).ifPresent(existingProduct::setAdditionalNotes);

            //valido que productType exista en la BD antes de asignarlo (decisión de diseño que no sea CASCADE)
            Optional.ofNullable(productEntity.getProductType()).ifPresent(newProductType -> {
                ProductTypeEntity existingProductType = productTypeRepository.findById(newProductType.getId())
                        .orElseThrow(() -> new RuntimeException("El tipo de producto no existe."));
                existingProduct.setProductType(existingProductType);
            });

            //actualizo product recuperado
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("El producto no existe.")); //no se encontró en bd
    }

    @Override
    public void delete(Long id) { productRepository.deleteById(id); }
}
