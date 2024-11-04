package com.imavazq.public_business_api_rest.controller;

import com.imavazq.public_business_api_rest.domain.dto.ProductDto;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import com.imavazq.public_business_api_rest.service.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private IMapper<ProductEntity, ProductDto> productMapper;
    private IProductService productService;

    public ProductController(
            IMapper<ProductEntity, ProductDto> productMapper, IProductService productService
    ) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @PostMapping(path = "/api/v1/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity savedProductEntity = productService.save(productEntity);

        ProductDto savedProductDto = productMapper.mapTo(savedProductEntity);
        // devuelvo el productDto con Status HTTP 201
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/products")
    public List<ProductDto> listProducts(){
        List<ProductEntity> products = productService.findAll();

                //paso lista a stream para poder operar
        return products.stream()
                //mapeo toda la lista a DTO
                .map(productMapper::mapTo)
                //convertimos a List nuevamente
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/api/v1/product/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id){
        Optional<ProductEntity> foundProduct = productService.findOne(id);

        return foundProduct.map(productEntity -> { //Si Optional tiene algo:
            ProductDto productDto = productMapper.mapTo(productEntity);
            return new ResponseEntity<>(productDto, HttpStatus.OK); //Devuelvo dto con status 201 OK
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //Si Optional está vacío devuelvo sólo status 404 NOT FOUND
    }

    @PutMapping(path = "/api/v1/product/{id}")
    public ResponseEntity<ProductDto> fullUpdateProduct(
            @PathVariable("id") Long id, @RequestBody ProductDto productDto
    ){
        if(!productService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no existe --> 404 not found
        }

        productDto.setId(id); //aseguro que id no cambie
        ProductEntity productEntity = productMapper.mapFrom(productDto);

        //Hago UPDATE
        ProductEntity savedProductEntity = productService.save(productEntity);
        ProductDto savedProductDto = productMapper.mapTo(savedProductEntity);

        return new ResponseEntity<>(savedProductDto, HttpStatus.OK);
    }

    @PatchMapping(path = "/api/v1/product/{id}")
    public ResponseEntity<ProductDto> partialUpdateProduct(
            @PathVariable("id") Long id, @RequestBody ProductDto productDto
    ){
        if(!productService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no existe --> 404 not found
        }

        ProductEntity productEntity = productMapper.mapFrom(productDto);

        //Hago partial UPDATE
        ProductEntity updatedProductEntity = productService.partialUpdate(id, productEntity);
        ProductDto updatedProductDto = productMapper.mapTo(updatedProductEntity);

        return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/v1/product/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Long id){
        productService.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //status 204 NO CONTENT
    }
}
