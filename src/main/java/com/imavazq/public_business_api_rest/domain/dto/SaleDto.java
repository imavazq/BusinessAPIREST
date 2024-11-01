package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imavazq.public_business_api_rest.domain.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
public class SaleDto {
    private Long id;

    @JsonProperty("total_amount")
    private Integer amount;

    @JsonProperty("unit_price")
    private Float unitPrice;

    @JsonProperty("total_price")
    private Float totalPrice;

    private LocalDateTime date;

    private ProductDto product;
}
