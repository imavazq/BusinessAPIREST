package com.imavazq.public_business_api_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.dto.EntryDto;
import com.imavazq.public_business_api_rest.domain.dto.ProductDto;
import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.entity.EntryEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import com.imavazq.public_business_api_rest.service.IEntryService;
import com.imavazq.public_business_api_rest.service.IProductService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class EntryControllerIntegrationTest {
    private MockMvc mockMvc;
    private IEntryService entryService;
    private IProductTypeService productTypeService;
    private IProductService productService;
    private ObjectMapper objectMapper;
    //En lugar de implementar mappers de entidades implemento DTOs del mismo tipo en TestDataUtil
    //private IMapper<ProductEntity, ProductDto> productMapper;
    //private IMapper<EntryEntity, EntryDto> entryMapper;

    @Autowired
    public EntryControllerIntegrationTest(
            MockMvc mockMvc, IProductTypeService productTypeService, IEntryService entryService,
            IProductService productService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.productTypeService = productTypeService;
        this.entryService = entryService;
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    //Funciones setup
    public ProductEntity almacenarProduct(){
        ProductTypeEntity testProductTypeA = TestDataUtil.createTestProductTypeA();
        productTypeService.save(testProductTypeA);
        ProductEntity testProductA = TestDataUtil.createTestProductA(testProductTypeA);
        return productService.save(testProductA);
    }

    public EntryEntity almacenarEntry(){
        ProductEntity savedProduct = almacenarProduct();

        //Inserto entry en bd
        EntryEntity testEntryA = TestDataUtil.createTestEntryA(savedProduct);
        return entryService.save(testEntryA);
    }

    //Tests CREATE
    @Test
    public void testThatCreateEntrySuccessfullyReturnsHttp201Created() throws Exception {
        ProductEntity savedProduct = almacenarProduct();

        //Podría implementar mapper de savedProduct directamente..
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);
        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateEntrySuccessfullyReturnsSavedEntry() throws Exception {
        ProductEntity savedProduct = almacenarProduct();

        //Podría implementar mapper de savedProduct directamente..
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);
        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_amount").value(testEntryDtoA.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.unit_cost").value(testEntryDtoA.getUnitCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_cost").value(testEntryDtoA.getTotalCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.date").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product.id").value(testEntryDtoA.getProduct().getId())
        );
    }

    //Tests READ MANY
    @Test
    public void testThatListEntriesReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/entries")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListEntriesReturnsListOfEntries() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/entries")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]id").value(savedEntry.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]total_amount").value(savedEntry.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]unit_cost").value(savedEntry.getUnitCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]total_cost").value(savedEntry.getTotalCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]date").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]product.id").value(savedEntry.getId())
        );
    }

    //Test READ ONE
    @Test
    public void testThatGetEntryReturnsHttpStatus200WhenEntryExists() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetEntryReturnsHttpStatus404WhenNoEntryExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetEntryReturnsEntryWhenEntryExists() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_amount").value(savedEntry.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.unit_cost").value(savedEntry.getUnitCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_cost").value(savedEntry.getTotalCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.date").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product.id").value(savedEntry.getProduct().getId())
        );
    }

    //Tests FULL UPDATE
    //TODO: Centralizar en una funcion el setup de FullUpdate test
    @Test
    public void testThatFullUpdateEntryReturnsHttpStatus200WhenEntryExists() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD

        //Cambio el estado del entry menos product referenciado
        EntryDto testEntryDtoB = TestDataUtil.createTestEntryDtoB(testProductDtoA);
        testEntryDtoB.setId(savedEntry.getId());//aseguro mismo id
        String entryJson = objectMapper.writeValueAsString(testEntryDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/entry/" + savedEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateEntryReturnsHttpStatus404WhenNoEntryExists() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);

        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateUpdatesExistingEntry() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD

        //Cambio el estado del entry menos product referenciado
        EntryDto testEntryDtoB = TestDataUtil.createTestEntryDtoB(testProductDtoA);
        testEntryDtoB.setId(savedEntry.getId());//aseguro mismo id
        String entryJson = objectMapper.writeValueAsString(testEntryDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/entry/" + savedEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedEntry.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_amount").value(testEntryDtoB.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.unit_cost").value(testEntryDtoB.getUnitCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_cost").value(testEntryDtoB.getTotalCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.date").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product.id").value(testEntryDtoB.getProduct().getId())
        );
    }

    //Tests PARTIAL UPDATE
    @Test
    public void testThatPartialUpdateReturnsHttpStatus200WhenEntryExists() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        //Cambio parte del estado del entry
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);
        testEntryDtoA.setAmount(1000);
        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/entry/" + savedEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus404WhenNoEntryExists() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);
        testEntryDtoA.setAmount(1000);
        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateEntryReturnsUpdatedEntry() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA); //Uso mismo product para no tener que almacenarlo en BD
        EntryDto testEntryDtoA = TestDataUtil.createTestEntryDtoA(testProductDtoA);
        //cambio algunos valores
        testEntryDtoA.setAmount(1000);
        testEntryDtoA.setUnitCost(1000.0F);
        String entryJson = objectMapper.writeValueAsString(testEntryDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/entry/" + savedEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entryJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedEntry.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_amount").value(testEntryDtoA.getAmount())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.unit_cost").value(testEntryDtoA.getUnitCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.total_cost").value(savedEntry.getTotalCost())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.date").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product.id").value(savedEntry.getProduct().getId())
        );
    }

    //Tests DELETE
    @Test
    public void testThatDeleteEntryReturnsHttpStatus204WhenEntryExists() throws Exception {
        EntryEntity savedEntry = almacenarEntry();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/entry/" + savedEntry.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent() //204 no content
        );
    }

    @Test
    public void testThatDeleteEntryReturnsHttpStatus204WhenNoEntryExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/entry/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
