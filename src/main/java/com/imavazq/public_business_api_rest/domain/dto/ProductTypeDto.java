package com.imavazq.public_business_api_rest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
public class ProductTypeDto {
    private Long id;

    private String description;
}
