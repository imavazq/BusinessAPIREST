package com.imavazq.public_business_api_rest;

import com.imavazq.public_business_api_rest.domain.dto.EntryDto;
import com.imavazq.public_business_api_rest.domain.dto.ProductDto;
import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.dto.SaleDto;
import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.domain.entity.SaleEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TestDataUtil {
    private TestDataUtil() {
    }

    public static ProductTypeEntity createTestProductTypeA() {
        return ProductTypeEntity.builder()
                //.id(1L)
                .description("Tipo A")
                .build();
    }

    public static ProductTypeDto createTestProductTypeDtoA(){
        return ProductTypeDto.builder()
                //.id(1L)
                .description("Tipo A")
                .build();
    }

    public static ProductTypeEntity createTestProductTypeB(){
        return ProductTypeEntity.builder()
                //.id(2L)
                .description("Tipo B")
                .build();
    }

    public static ProductTypeDto createTestProductTypeDtoB() {
        return ProductTypeDto.builder()
                //.id(2L)
                .description("Tipo B")
                .build();
    }

    public static ProductEntity createTestProductA(final ProductTypeEntity productTypeEntity){
        return ProductEntity.builder()
                //.id(1L)
                .article("artA")
                .size("S")
                .description("asd")
                .price(123.3F)
                .stockAvailable(0)
                .additionalNotes("asd")
                .productType(productTypeEntity)
                .build();
    }

    public static ProductDto createTestProductDtoA(final ProductTypeDto productTypeDto){
        return ProductDto.builder()
                //.id(1L)
                .article("artA")
                .size("S")
                .description("asd")
                .price(123.3F)
                .stockAvailable(0)
                .additionalNotes("asd")
                .productType(productTypeDto)
                .build();
    }

    public static ProductEntity createTestProductB(final ProductTypeEntity productTypeEntity){
        return ProductEntity.builder()
                //.id(2L)
                .article("artB")
                .size("M")
                .description("asd")
                .price(100F)
                .stockAvailable(0)
                .additionalNotes("asd")
                .productType(productTypeEntity)
                .build();
    }

    public static ProductDto createTestProductDtoB(ProductTypeDto productTypeDto) {
        return ProductDto.builder()
                //.id(2L)
                .article("artB")
                .size("M")
                .description("asd")
                .price(100F)
                .stockAvailable(0)
                .additionalNotes("asd")
                .productType(productTypeDto)
                .build();
    }

    public static EntryEntity createTestEntryA(final ProductEntity productEntity){
        return EntryEntity.builder()
                //.id(1L)
                .amount(1)
                .unitCost(111.1F)
                //.totalCost(111.1F)
                //.date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productEntity)
                .build();
    }

    public static EntryDto createTestEntryDtoA(final ProductDto productDto){
        return EntryDto.builder()
                //.id(1L)
                .amount(1)
                .unitCost(111.1F)
                //.totalCost(111.1F)
                //.date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productDto)
                .build();
    }

    public static EntryEntity createTestEntryB(final ProductEntity productEntity){
        return EntryEntity.builder()
                //.id(2L)
                .amount(2)
                .unitCost(222.2F)
                //.totalCost(444.4F)
                //.date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productEntity)
                .build();
    }

    public static EntryDto createTestEntryDtoB(final ProductDto productDto){
        return EntryDto.builder()
                //.id(2L)
                .amount(2)
                .unitCost(222.2F)
                //.totalCost(444.4F)
                //.date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productDto)
                .build();
    }

    public static SaleEntity createTestSaleA(final ProductEntity productEntity){
        return SaleEntity.builder()
                .id(1L)
                .amount(1)
                .unitPrice(111.1F)
                .totalPrice(111.1F)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productEntity)
                .build();
    }

    public static SaleDto createTestSaleDtoA(final ProductDto productDto){
        return SaleDto.builder()
                .id(1L)
                .amount(1)
                .unitPrice(111.1F)
                .totalPrice(111.1F)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productDto)
                .build();
    }

    public static SaleEntity createTestSaleB(final ProductEntity productEntity){
        return SaleEntity.builder()
                .id(2L)
                .amount(2)
                .unitPrice(222.2F)
                .totalPrice(444.4F)
                .date(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .product(productEntity)
                .build();
    }



}
