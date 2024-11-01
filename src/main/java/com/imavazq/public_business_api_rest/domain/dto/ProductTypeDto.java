package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductTypeDto {
    private Long id;

    @JsonProperty("desc")
    private String description;
}
