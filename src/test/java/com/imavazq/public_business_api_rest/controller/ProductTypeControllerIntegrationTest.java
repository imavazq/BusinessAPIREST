package com.imavazq.public_business_api_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.service.IProductTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc//crea instancia de mock MVC y la coloca en app context
public class ProductTypeControllerIntegrationTest {
    private MockMvc mockMvc;
    private IProductTypeService productTypeService;
    //Para mappear entre DTO y string necesario para mandar en body (.content()) de request mock
    private ObjectMapper objectMapper;

    @Autowired
    public ProductTypeControllerIntegrationTest(MockMvc mockMvc, IProductTypeService productTypeService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.productTypeService = productTypeService;
        this.objectMapper = objectMapper;
    }

    //Tests CREATE
    @Test
    public void testThatCreateProductTypeSuccessfullyReturnsHttp201Created() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        testProductTypeDtoA.setId(null); //dejo que se lo asigne la bd

        //paso dto a string para poder pasarlo por body de request
        String productTypeJson = objectMapper.writeValueAsString(testProductTypeDtoA);

        //simulo http request pasando por body el json de testProductTypeDtoA
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/productType")
                        .contentType(MediaType.APPLICATION_JSON) //especifico formato json
                        .content(productTypeJson)
        ).andExpect( //asserts
                MockMvcResultMatchers.status().isCreated() //valido que devuelva status 201 created
        );
    }

    @Test
    public void testThatCreateProductTypeSuccessfullyReturnsSavedProductType() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        testProductTypeDtoA.setId(null);

        String productTypeJson = objectMapper.writeValueAsString(testProductTypeDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/productType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeJson)
        ).andExpect( //asserts
                MockMvcResultMatchers.jsonPath("$.id").isNumber() //valido que id sea un número
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductTypeDtoA.getDescription()) //valido que desc sea la almacenada
        );
    }

    //Tests READ MANY
    @Test
    public void testThatListProductTypesReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/productTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                //no es necesario .content() porque no enviamos body
        ).andExpect(MockMvcResultMatchers.status().isOk()); //valido que devuelva status 200 ok
    }

    @Test
    public void testThatListProductTypesReturnsListOfProductTypes() throws Exception {
        ProductTypeEntity testProductTypeA = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeA); //agregamos un productType a la BD

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/productTypes")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]id").isNumber() //valido que id del primero de la lista [0] sea un número
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]desc").value(testProductTypeA.getDescription())
        );
    }

    //Test READ ONE
    @Test
    public void testThatGetProductTypeReturnsHttpStatus200WhenProductTypeExists() throws Exception {
        ProductTypeEntity testProductTypeA = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeA); //agregamos un productType a la BD

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
                //no es necesario .content() porque no enviamos body
        ).andExpect(MockMvcResultMatchers.status().isOk()); //valido que devuelva status 200 ok
    }

    @Test
    public void testThatGetProductTypeReturnsHttpStatus404WhenNoProductTypeExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
                //no es necesario .content() porque no enviamos body
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); //valido que devuelva status 404 not found
    }

    @Test
    public void testThatGetProductTypeReturnsProductTypeWhenProductTypeExists() throws Exception {
        ProductTypeEntity testProductTypeA = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeA); //agregamos un productType a la BD

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductTypeA.getDescription())
        );
    }

    //Test FULL UPDATE
    @Test
    public void testThatFullUpdateProductTypeReturnsHttpStatus200WhenProductTypeExists() throws Exception {
        ProductTypeEntity testProductTypeA = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeA); //almaceno en BD

        //creo productType dto con mismo id para usar su body en update
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        String productTypeDtoJson = objectMapper.writeValueAsString(testProductTypeDtoA); //convierto a json para pasar en body

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/productType/" + testProductTypeA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk()); //valido que devuelva status 200 ok
    }

    @Test
    public void testThatFullUpdateProductTypeReturnsHttpStatus404WhenNoProductTypeExists() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        String productTypeDtoJson = objectMapper.writeValueAsString(testProductTypeDtoA); //convierto a json para pasar en body

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); //valido que devuelva status 404 not found
    }

    @Test
    public void testThatFullUpdateUpdatesExistingProductType() throws Exception {
        ProductTypeEntity testProductTypeAEntity = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeAEntity);

        //creo un productType distinto para hacer update con sus datos
        ProductTypeDto testProductTypeDtoB = TestDataUtil.createTestProductTypeDtoB();
        testProductTypeDtoB.setId(testProductTypeAEntity.getId()); //aseguro mismo id
        String productTypeDtoUpdateJson = objectMapper.writeValueAsString(testProductTypeDtoB); //convierto a json para pasar en body

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/productType/" + testProductTypeAEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testProductTypeAEntity.getId()) //valido que sea mismo id que original
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductTypeDtoB.getDescription()) //valido que haya actualizado desc
        );
    }


    //Tests PARTIAL UPDATE
    @Test
    public void testThatPartialUpdateReturnsHttpStatus200WhenProductTypeExists() throws Exception {
        ProductTypeEntity testProductTypeAEntity = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeAEntity);

        ProductTypeDto testProductTypeADto = TestDataUtil.createTestProductTypeDtoA(); //creo el mismo productType pero DTO (podría mapear directamente)
        testProductTypeADto.setDescription("UPDATED"); //actualizo un atributo
        String productTypeDtoJson = objectMapper.writeValueAsString(testProductTypeADto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/productType/" + testProductTypeADto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk()); //valido que devuelva status 200 ok
    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus404WhenNoProductTypeExists() throws Exception {
        ProductTypeDto testProductTypeADto = TestDataUtil.createTestProductTypeDtoA();
        String productTypeDtoJson = objectMapper.writeValueAsString(testProductTypeADto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); //valido que devuelva status 404 not found
    }

    @Test
    public void testThatPartialUpdateProductTypeReturnsUpdatedProductType() throws Exception {
        ProductTypeEntity testProductTypeAEntity = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeAEntity);

        ProductTypeDto testProductTypeADto = TestDataUtil.createTestProductTypeDtoA(); //creo el mismo productType pero DTO (podría mapear directamente)
        testProductTypeADto.setDescription("UPDATED"); //actualizo un atributo
        String productTypeDtoJson = objectMapper.writeValueAsString(testProductTypeADto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/productType/" + testProductTypeAEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTypeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testProductTypeAEntity.getId()) //valido que sea mismo id
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductTypeADto.getDescription()) //valido que se haya actualizado descripcion
        );
    }

    //Tests DELETE
    @Test
    public void testThatDeleteProductTypeReturnsHttpStatus204WhenProductTypeExists() throws Exception {
        ProductTypeEntity testProductTypeEntity = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeEntity); //almaceno en bd el productType

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/productType/" + testProductTypeEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()); //valido que devuelva status 204 no content
    }

    @Test
    public void testThatDeleteProductTypeReturnsHttpStatus204WhenNoProductTypeExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/productType/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()); //valido que devuelva status 204 no content
    }
}