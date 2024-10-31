package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
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
public class EntryRepositoryIntegrationTests {
    private EntryRepository underTest;
    private ProductTypeRepository productTypeRepository;
    private ProductRepository productRepository;

    @Autowired
    public EntryRepositoryIntegrationTests(EntryRepository underTest, ProductTypeRepository productTypeRepository, ProductRepository productRepository) {
        this.underTest = underTest;
        this.productTypeRepository = productTypeRepository;
        this.productRepository = productRepository;
    }

    @Test
    public void testThatEntryCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA(); //creo primero el productType para asignárselo a product
        productTypeRepository.save(productTypeEntity); //almaceno productType primero xq lo necesito para asignárselo al product
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity); //almaceno product con referencia al productType creado

        EntryEntity entryEntity = TestDataUtil.createTestEntryA(productEntity);
        underTest.save(entryEntity);//almaceno entry en bd

        Optional<EntryEntity> result = underTest.findById(entryEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(entryEntity);
    }

    @Test
    public void testThatMultipleEntriesCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        EntryEntity entryEntityA = TestDataUtil.createTestEntryA(productEntity);
        underTest.save(entryEntityA);//almaceno entry en bd
        EntryEntity entryEntityB = TestDataUtil.createTestEntryB(productEntity);
        underTest.save(entryEntityB);//almaceno entry en bd

        Iterable<EntryEntity> result = underTest.findAll();
        Assertions.assertThat(result)
                .hasSize(2) //valido que tenga exactamente 2
                .contains(entryEntityA, entryEntityB); //valido que contenga ambos entry almacenados
    }

    @Test
    public void testThatEntryCanBeUpdated(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        EntryEntity entryEntity = TestDataUtil.createTestEntryA(productEntity);
        underTest.save(entryEntity);//almaceno entry en bd

        entryEntity.setAmount(2);
        underTest.save(entryEntity);//actualizo entry almacenado antes

        Optional<EntryEntity> result = underTest.findById(entryEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(entryEntity);
        assertThat(result.get().getAmount()).isEqualTo(2);
    }

    @Test
    public void testThatEntryCanBeDeleted(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        productRepository.save(productEntity);

        EntryEntity entryEntity = TestDataUtil.createTestEntryA(productEntity);
        underTest.save(entryEntity);//almaceno entry en bd

        underTest.deleteById(entryEntity.getId());//elimino entry de la bd
        Optional<EntryEntity> result = underTest.findById(entryEntity.getId());
        assertThat(result).isEmpty();//valido que se haya eliminado de la BD
    }
}
