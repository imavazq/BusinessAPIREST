package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
public class ProductDto {
    private Long id;

    private String article;

    private String size;

    @JsonProperty("desc")
    private String description;

    private Float price;

    @JsonProperty("stock_available")
    private Integer stockAvailable;

    @JsonProperty("additional_notes")
    private String additionalNotes;

    @JsonProperty("product_type")
    private ProductTypeDto productType;
}
