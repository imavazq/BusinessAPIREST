package com.imavazq.public_business_api_rest.repository;

import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryIntegrationTests {
    private ProductRepository underTest;
    private ProductTypeRepository productTypeRepository;

    //private static Logger logger = LoggerFactory.getLogger(ProductRepositoryIntegrationTests.class);

    @Autowired
    public ProductRepositoryIntegrationTests(ProductRepository underTest, ProductTypeRepository productTypeRepository) {
        this.underTest = underTest;
        this.productTypeRepository = productTypeRepository;
    }

    @Test
    public void testThatProductCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA(); //creo primero el productType para asignárselo a product
        productTypeRepository.save(productTypeEntity); //almaceno productType primero xq lo necesito para asignárselo al product

        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        //logger.debug("PRE");
        underTest.save(productEntity); //almaceno product con referencia al productType creado
        //logger.debug("POST");

        Optional<ProductEntity> result = underTest.findById(productEntity.getId()); //recupero de BD

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntity);
    }

    @Test
    public void testThatMultipleProductsCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);

        ProductEntity productEntityA = TestDataUtil.createTestProductA(productTypeEntity);
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductB(productTypeEntity);
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAll();

        assertThat(result)
                .hasSize(2) //valido que tenga exactamente 2
                .contains(productEntityA, productEntityB); //valido que contenga ambos product almacenados
    }

    @Test
    public void testThatProductCanBeUpdated(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        underTest.save(productEntity);

        productEntity.setArticle("UPDATED");
        underTest.save(productEntity);

        Optional<ProductEntity> result = underTest.findById(productEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntity);
        assertThat(result.get().getArticle()).isEqualTo("UPDATED"); //valido que tenga art actualizado
    }

    @Test
    public void testThatProductCanBeDeletedAndItsProductTypeCanStillBeRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeRepository.save(productTypeEntity);
        ProductEntity productEntity = TestDataUtil.createTestProductA(productTypeEntity);
        underTest.save(productEntity);

        underTest.deleteById(productEntity.getId());
        Optional<ProductEntity> result = underTest.findById(productEntity.getId());
        assertThat(result).isEmpty(); //valido que se haya eliminado el product de la bd


        Optional<ProductTypeEntity> result2 = productTypeRepository.findById(productTypeEntity.getId());
        assertThat(result2).isPresent();
        assertThat(result2.get()).isEqualTo(productTypeEntity); //valido que no se haya eliminado por cascada el productType de la bd
    }
}