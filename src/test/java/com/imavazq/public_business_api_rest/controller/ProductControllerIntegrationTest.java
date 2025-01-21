package com.imavazq.public_business_api_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imavazq.public_business_api_rest.TestDataUtil;
import com.imavazq.public_business_api_rest.domain.dto.ProductDto;
import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import com.imavazq.public_business_api_rest.service.IProductService;
import com.imavazq.public_business_api_rest.service.IProductTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
@AutoConfigureMockMvc//crea instancia de mock MVC y la coloca en app context
public class ProductControllerIntegrationTest {
    private MockMvc mockMvc;

    private IProductService productService;
    private IProductTypeService productTypeService;

    private ObjectMapper objectMapper;
    private IMapper<ProductTypeEntity, ProductTypeDto> productTypeMapper;

    @Autowired
    public ProductControllerIntegrationTest(
            MockMvc mockMvc,
            IProductService productService,
            IProductTypeService productTypeService,
            ObjectMapper objectMapper,
            IMapper<ProductTypeEntity, ProductTypeDto> productTypeMapper) {
        this.mockMvc = mockMvc;
        this.productService = productService;
        this.productTypeService = productTypeService;
        this.objectMapper = objectMapper;
        this.productTypeMapper = productTypeMapper;
    }

    // método que crea productType en BD y lo devuelve como DTO para utilizarlo como test
    //@BeforeEach no lo uso porque no necesito en todos los tests crear un product
    public ProductTypeDto createsAndReturnsTestProductTypeDto(){
        ProductTypeEntity productTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(productTypeEntityA);

        return productTypeMapper.mapTo(savedProductTypeEntity);
    }

    //Tests CREATE
    @Test
    public void testThatCreateProductSuccessfullyReturnsHttp201Created() throws Exception {
        //primero creo productType y lo almaceno en la BD
        ProductTypeDto savedProductTypeDto = createsAndReturnsTestProductTypeDto();

        //asigno productTypeDto porque lo creo a través del cliente
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(savedProductTypeDto);
        testProductDtoA.setId(null); //dejo que se lo asigne la bd

        //paso dto a string para poder pasarlo por body de request
        String productJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateProductSuccessfullyReturnsSavedProduct() throws Exception {
        //primero creo productType y lo almaceno en la BD
        ProductTypeDto savedProductTypeDto = createsAndReturnsTestProductTypeDto();

        //asigno productTypeDto porque lo creo a través del cliente
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(savedProductTypeDto);
        testProductDtoA.setId(null); //dejo que se lo asigne la bd

        //paso dto a string para poder pasarlo por body de request
        String productJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber() //valido que id sea un número
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.article").value(testProductDtoA.getArticle()) //valido que article sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(testProductDtoA.getSize()) //valido que size sea el almacenado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductDtoA.getDescription()) //valido que desc sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(testProductDtoA.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.stock_available").value(testProductDtoA.getStockAvailable())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.additional_notes").value(testProductDtoA.getAdditionalNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product_type.id").value(testProductDtoA.getProductType().getId()) //valido que id de productType referenciado corresponda
        );
    }

    //Tests READ MANY
    @Test
    public void testThatListProductsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListProductsReturnsListOfProducts() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno products
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        ProductEntity testProductEntityB = TestDataUtil.createTestProductB(savedProductTypeEntity);
        productService.save(testProductEntityA);
        productService.save(testProductEntityB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]id").value(testProductEntityA.getId()) //valido que id sea un número
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]article").value(testProductEntityA.getArticle()) //valido que article sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]size").value(testProductEntityA.getSize()) //valido que size sea el almacenado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]desc").value(testProductEntityA.getDescription()) //valido que desc sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]price").value(testProductEntityA.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]stock_available").value(testProductEntityA.getStockAvailable())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]additional_notes").value(testProductEntityA.getAdditionalNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0]product_type.id").value(testProductEntityA.getProductType().getId()) //valido que id de productType referenciado corresponda
        );
    }

    //Test READ ONE
    @Test
    public void testThatGetProductReturnsHttpStatus200WhenProductExists() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno products
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk() //valido que devuelva status 200 ok
        );
    }

    @Test
    public void testThatGetProductReturnsHttpStatus404WhenNoProductExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound() //valido que devuelva status 404 not found
        );
    }

    @Test
    public void testThatGetProductReturnsProductWhenProductExists() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno products
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber() //valido que id sea un número
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.article").value(testProductEntityA.getArticle()) //valido que article sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(testProductEntityA.getSize()) //valido que size sea el almacenado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductEntityA.getDescription()) //valido que desc sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(testProductEntityA.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.stock_available").value(testProductEntityA.getStockAvailable())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.additional_notes").value(testProductEntityA.getAdditionalNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product_type.id").value(testProductEntityA.getProductType().getId()) //valido que id de productType referenciado corresponda
        );
    }

    //Test FULL UPDATE
    @Test
    public void testThatFullUpdateProductReturnsHttpStatus200WhenProductExists() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno products
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        //creo product dto con mismo productType (mismo id que el almacenado) para usar su body en update
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        testProductTypeDtoA.setId(1L); //Aseguro que id de productType sea el mismo que el almacenado
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA);
        //convierto a json con formato dto para pasar en body
        String productDtoJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/product/" + testProductEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk() //valido que devuelva status 200 ok
        );
    }

    @Test
    public void testThatFullUpdateProductReturnsHttpStatus404WhenNoProductExists() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA);
        //convierto a json con formato dto para pasar en body
        String productDtoJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/product/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound() //valido que devuelva status 404 Not found
        );
    }

    @Test
    public void testThatFullUpdateUpdatesExistingProduct() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno product
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        //creo product distinto al almacenado en bd
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA(); //uso mismo productType para no volver a guardar en BD
        testProductTypeDtoA.setId(1L); //Aseguro que id de productType sea el mismo que el almacenado

        ProductDto testProductDtoB = TestDataUtil.createTestProductDtoB(testProductTypeDtoA);
        //testProductDtoB.setId(testProductEntityA.getId()); //aseguro mismo id

        String productDtoUpdateJson = objectMapper.writeValueAsString(testProductDtoB);//convierto a json para pasar en body

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/product/" + testProductEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testProductEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.article").value(testProductDtoB.getArticle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(testProductDtoB.getSize()) //valido que size sea el almacenado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductDtoB.getDescription()) //valido que desc sea la almacenada
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(testProductDtoB.getPrice())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.stock_available").value(testProductDtoB.getStockAvailable())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.additional_notes").value(testProductDtoB.getAdditionalNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product_type.id").value(testProductDtoB.getProductType().getId()) //valido que id de productType referenciado corresponda
        );
    }

    //Tests PARTIAL UPDATE
    @Test
    public void testThatPartialUpdateReturnsHttpStatus200WhenProductExists() throws Exception{
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno product
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        //creo product dto con mismo id y productType para usar su body en update
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        testProductTypeDtoA.setId(1L); //Aseguro que id de productType sea el mismo que el almacenado

        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA);
        testProductDtoA.setArticle("UPDATED"); //Actualizo un valor
        //convierto a json con formato dto para pasar en body
        String productDtoJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/product/" + testProductEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsHttpStatus404WhenNoProductExists() throws Exception {
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA);
        String productDtoJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound() //valido que devuelva status 404 not found
        );
    }

    @Test
    public void testThatPartialUpdateProductReturnsUpdatedProduct() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno product
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        //creo product dto con mismo id y productType para usar su body en update
        ProductTypeDto testProductTypeDtoA = TestDataUtil.createTestProductTypeDtoA();
        testProductTypeDtoA.setId(1L); //Aseguro que id de productType sea el mismo que el almacenado

        ProductDto testProductDtoA = TestDataUtil.createTestProductDtoA(testProductTypeDtoA);
        testProductDtoA.setArticle("UPDATED"); //Actualizo algunos valores
        testProductDtoA.setPrice(1000.0F);
        //convierto a json con formato dto para pasar en body
        String productDtoJson = objectMapper.writeValueAsString(testProductDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/product/" + testProductEntityA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testProductEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.article").value(testProductDtoA.getArticle()) //Valido que se corresponda al valor cambiado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.size").value(testProductEntityA.getSize())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.desc").value(testProductEntityA.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.price").value(testProductDtoA.getPrice()) //Valido que se corresponda al valor cambiado
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.stock_available").value(testProductEntityA.getStockAvailable())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.additional_notes").value(testProductEntityA.getAdditionalNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.product_type.id").value(testProductEntityA.getProductType().getId()) //valido que id de productType referenciado corresponda al original
        );
    }


    //Tests DELETE
    @Test
    public void testThatDeleteProductReturnsHttpStatus204WhenProductExists() throws Exception {
        //primero creo productType
        ProductTypeEntity testProductTypeEntityA = TestDataUtil.createTestProductTypeA();
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(testProductTypeEntityA);

        //creo y almaceno product
        ProductEntity testProductEntityA = TestDataUtil.createTestProductA(savedProductTypeEntity);
        productService.save(testProductEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/product/" + testProductEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent() //valido que devuelva status 204 no content
        );
    }

    @Test
    public void testThatDeleteProductReturnsHttpStatus204WhenNoProductExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent() //valido que devuelva status 204 no content
        );
    }
}