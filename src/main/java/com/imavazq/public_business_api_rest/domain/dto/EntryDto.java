package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imavazq.public_business_api_rest.domain.dto.groups.OnPartialUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.Default;
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
    @Null(message = "This field must be null", groups = {Default.class, OnPartialUpdate.class})
    private Long id;

    @NotNull(message = "Total amount is mandatory", groups = Default.class)
    @Positive(message = "The price must be positive", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("total_amount")
    private Integer amount;

    @NotNull(message = "Unit cost is mandatory", groups = Default.class)
    @PositiveOrZero(message = "The unit cost must be positive or zero", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("unit_cost")
    private Float unitCost;

    @Null(message = "This field must be null", groups = {Default.class, OnPartialUpdate.class})
    //@NotNull(message = "Total cost is mandatory", groups = Default.class)
    //@PositiveOrZero(message ="The total cost must be positive or zero", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("total_cost")
    private Float totalCost;

    @Null(message = "This field must be null", groups = {Default.class, OnPartialUpdate.class})
    private LocalDateTime date;//Se agrega desde service //Tiene este formato para que coincida con el de la BD

    @NotNull(message="Product is mandatory", groups = Default.class)
    private ProductDto product;
}
