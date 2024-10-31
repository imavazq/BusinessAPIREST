package com.imavazq.public_business_api_rest.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
public class EntryDto {
    private Long id;

    private Integer amount;

    private Float unitCost;

    private Float totalCost;

    private LocalDateTime date;//Se agrega desde service //Tiene este formato para que coincida con el de la BD

    private ProductDto product;
}