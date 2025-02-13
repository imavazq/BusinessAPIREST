package com.imavazq.public_business_api_rest.controller;

import com.imavazq.public_business_api_rest.domain.dto.ProductTypeDto;
import com.imavazq.public_business_api_rest.domain.dto.groups.OnPartialUpdate;
import com.imavazq.public_business_api_rest.domain.entity.ProductTypeEntity;
import com.imavazq.public_business_api_rest.mapper.IMapper;
import com.imavazq.public_business_api_rest.service.IProductTypeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController//Especificamos que se trata de una rest API
@Tag(name = "Product Type")
public class ProductTypeController {
    private IProductTypeService productTypeService;
    private IMapper<ProductTypeEntity, ProductTypeDto> productTypeMapper;

    //Inyecto beans que implementan las interfaces (no necesario @Autowired)
    //@Autowired
    public ProductTypeController(
            IProductTypeService productTypeService, IMapper<ProductTypeEntity, ProductTypeDto> productTypeMapper
    ) {
        this.productTypeService = productTypeService;
        this.productTypeMapper = productTypeMapper;
    }

    @Operation(
            description = "Post endpoint for Product Type",
            summary = "Creation of a product type",
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201"
                    )
            },
            deprecated = false
    )
    @PostMapping(path = "/api/v1/productType")
    //@Hidden //por si quisiera ocultarlo
    public ResponseEntity<ProductTypeDto> createProductType(@Valid @RequestBody ProductTypeDto productTypeDto){
        ProductTypeEntity productTypeEntity = productTypeMapper.mapFrom(productTypeDto);//Pasamos de DTO a Entity
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(productTypeEntity);
        //Derivamos a service para que maneje la creación en la BD (lo hace realmente capa Repository)

        //Devolvemos el productTypeDto con Status HTTP 201
        return new ResponseEntity<>(productTypeMapper.mapTo(savedProductTypeEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/api/v1/productTypes")
    public List<ProductTypeDto> listProductTypes(){
        List<ProductTypeEntity> productTypes = productTypeService.findAll();

        return productTypes.stream()//paso lista a stream para poder operar
                .map(productTypeMapper::mapTo)//mapeo toda la lista a DTO
                .collect(Collectors.toList());//convertimos a List nuevamente
    }

    @GetMapping(path = "/api/v1/productType/{id}")
    public ResponseEntity<ProductTypeDto> getProductType(@PathVariable("id") Long id){
        Optional<ProductTypeEntity> foundProductType = productTypeService.findOne(id);

        return foundProductType.map(productTypeEntity -> {  //Si Optional tiene algo:
            ProductTypeDto productTypeDto = productTypeMapper.mapTo(productTypeEntity); //Mapea productType encontrado a Dto
            return new ResponseEntity<>(productTypeDto, HttpStatus.OK);//Devuelvo dto con status 201 OK
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //Si Optional está vacío devuelvo sólo status 404 NOT FOUND
    }

    @PutMapping(path = "/api/v1/productType/{id}")
    public ResponseEntity<ProductTypeDto> fullUpdateProductType(
            @PathVariable("id") Long id, @Valid @RequestBody ProductTypeDto productTypeDto
    ){
        //Primero valido que el productType exista
        if (!productTypeService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no existe --> 404 not found
        }

        productTypeDto.setId(id); //aseguro que id no cambie
        ProductTypeEntity productTypeEntity = productTypeMapper.mapFrom(productTypeDto);

        //Hago UPDATE (mismo método que create pero devuelvo distinto http status)
        ProductTypeEntity savedProductTypeEntity = productTypeService.save(productTypeEntity);
        ProductTypeDto savedProductTypeDto = productTypeMapper.mapTo(savedProductTypeEntity);

        return new ResponseEntity<>(
                savedProductTypeDto,
                HttpStatus.OK //status 200 OK
        );
    }

    @PatchMapping(path = "/api/v1/productType/{id}")
    public ResponseEntity<ProductTypeDto> partialUpdateProductType(
            @PathVariable("id") Long id, @Validated(OnPartialUpdate.class) @RequestBody ProductTypeDto productTypeDto
    ){
        //Primero valido que el productType exista
        if (!productTypeService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no existe --> 404 not found
        }

        //productTypeDto.setId(id); //lo hago en service en este caso, no como en PUT (invoco a save())
        ProductTypeEntity productTypeEntity = productTypeMapper.mapFrom(productTypeDto);

        //Hago partial UPDATE
        ProductTypeEntity updatedProductType = productTypeService.partialUpdate(id, productTypeEntity);
        ProductTypeDto savedProductTypeDto = productTypeMapper.mapTo(updatedProductType);

        return new ResponseEntity<>(
                savedProductTypeDto,
                HttpStatus.OK //status 200 OK
        );
    }

    @DeleteMapping(path = "/api/v1/productType/{id}")
    public ResponseEntity deleteProductType(@PathVariable("id") Long id){
        productTypeService.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //status 204 NO CONTENT
    }
}
