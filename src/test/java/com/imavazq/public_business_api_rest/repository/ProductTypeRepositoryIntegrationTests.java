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

@SpringBootTest//Para que se ejecute al lanzar la app en modo Test
@ExtendWith(SpringExtension.class)//Para asegurarnos que tod(o) esté integrado y soportado
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)// Para limpiar el contexto (incl BD) antes de cada método test
public class ProductTypeRepositoryIntegrationTests {
    private ProductTypeRepository underTest;

    @Autowired//Para que enlace con clase Bean ProductTypeRepository (@Repository)
    public ProductTypeRepositoryIntegrationTests(ProductTypeRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatProductTypeCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();

        underTest.save(productTypeEntity);//almaceno en BD
        Optional<ProductTypeEntity> result = underTest.findById(productTypeEntity.getId()); //recupero de BD

        assertThat(result).isPresent(); //valido que haya devuelto algo
        assertThat(result.get()).isEqualTo(productTypeEntity); //valido que sea el mismo productType almacenado
    }

    @Test
    public void testThatMultipleProductsTypeCanBeCreatedAndRecalled(){
        ProductTypeEntity productTypeEntityA = TestDataUtil.createTestProductTypeA();
        underTest.save(productTypeEntityA);
        ProductTypeEntity productTypeEntityB = TestDataUtil.createTestProductTypeB();
        underTest.save(productTypeEntityB);

        Iterable<ProductTypeEntity> result = underTest.findAll(); //recupero todos los productType de la BD
        assertThat(result)
                .hasSize(2) //valido que tenga exactamente 2
                .contains(productTypeEntityA, productTypeEntityB); //valido que contenga ambos productType almacenados
    }

    @Test
    public void testThatProductTypeCanBeUpdated(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        underTest.save(productTypeEntity);

        productTypeEntity.setDescription("UPDATED");//actualizo desc de java object
        underTest.save(productTypeEntity);//actualizo productType almacenado antes

        Optional<ProductTypeEntity> result = underTest.findById(productTypeEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productTypeEntity);
        assertThat(result.get().getDescription()).isEqualTo("UPDATED"); //valido que tenga desc actualizada
    }

    @Test
    public void testThatProductTypeCanBeDeleted(){
        ProductTypeEntity productTypeEntity = TestDataUtil.createTestProductTypeA();
        underTest.save(productTypeEntity);

        underTest.deleteById(productTypeEntity.getId()); //elimino productType almacenado antes
        Optional<ProductTypeEntity> result = underTest.findById(productTypeEntity.getId());
        assertThat(result).isEmpty(); //valido que se haya eliminado de la BD
    }
}
