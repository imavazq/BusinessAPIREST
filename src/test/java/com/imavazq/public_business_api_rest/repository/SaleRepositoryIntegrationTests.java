package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.domain.entity.SaleEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SaleRepositoryIntegrationTests {
    private SaleRepository underTest;
    private ProductTypeRepository productTypeRepository;
    private ProductRepository productRepository;

    @Autowired
    public SaleRepositoryIntegrationTests(SaleRepository underTest, ProductTypeRepository productTypeRepository, ProductRepository productRepository) {
        this.underTest = underTest;
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
    }

    @Test
    public void testThatSaleCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA(); //creo primero el productType para asignárselo a product
        productTypeRepository.save(productTypeEntity); //almaceno productType primero xq lo necesito para asignárselo al product
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity); //almaceno product con referencia al productType creado

        SaleEntity saleEntity = TestDataUtil.createTestSaleA(productEntity);
        underTest.save(saleEntity);//almaceno sale en bd

        Optional<SaleEntity> result = underTest.findById(saleEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(saleEntity);
    }

    @Test
    public void testThatMultipleSalesCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        SaleEntity saleEntityA = TestDataUtil.createTestSaleA(productEntity);
        underTest.save(saleEntityA);//almaceno sale en bd
        SaleEntity saleEntityB = TestDataUtil.createTestSaleB(productEntity);
        underTest.save(saleEntityB);//almaceno sale en bd

        Iterable<SaleEntity> result = underTest.findAll();
        Assertions.assertThat(result)
                .hasSize(2) //valido que tenga exactamente 2
                .contains(saleEntityA, saleEntityB); //valido que contenga ambos sale almacenados
    }

    @Test
    public void testThatSaleCanBeUpdated(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        SaleEntity saleEntity = TestDataUtil.createTestSaleA(productEntity);
        underTest.save(saleEntity);//almaceno sale en bd

        saleEntity.setAmount(2);
        underTest.save(saleEntity);//actualizo sale almacenado antes

        Optional<SaleEntity> result = underTest.findById(saleEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(saleEntity);
        assertThat(result.get().getAmount()).isEqualTo(2);
    }

    @Test
    public void testThatSaleCanBeDeleted(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        SaleEntity saleEntity = TestDataUtil.createTestSaleA(productEntity);
        underTest.save(saleEntity);//almaceno entry en bd

        underTest.deleteById(saleEntity.getId());//elimino entry de la bd
        Optional<SaleEntity> result = underTest.findById(saleEntity.getId());
        assertThat(result).isEmpty();//valido que se haya eliminado de la BD
    }
}
