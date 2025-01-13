package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imavazq.public_business_api_rest.domain.dto.groups.OnPartialUpdate;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.event.internal.OnUpdateVisitor;
import org.hibernate.validator.constraints.Length;

@Data//Getters,setter,hashcode,equals
@AllArgsConstructor
@NoArgsConstructor//Objetos Jackson siempre tiene que tener esta etiqueta
@Builder
public class ProductDto {
    @Null(message = "This field must be null", groups = {Default.class, OnPartialUpdate.class})
    private Long id;

    //@NotNull(message = "An article is mandatory")
    @NotBlank(message = "Article is mandatory", groups = Default.class)
    @Size(min = 1, max=10, message = "The article must contain from 1 to 10 characters", groups = {Default.class, OnPartialUpdate.class})
    private String article;

    //@NotNull(message = "Size is mandatory")
    @NotBlank(message = "Size is mandatory", groups = Default.class)
    @Size(min = 1, max = 5, message = "The size must contain from 1 to 5 characters", groups = {Default.class, OnPartialUpdate.class})
    private String size;

    @Size(min = 1, max = 100, message="The description must contain from 0 to 100 characters", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("desc")
    private String description;

    @NotNull(message = "Price is mandatory", groups = Default.class)
    @PositiveOrZero(message ="The price must be positive or zero", groups = {Default.class, OnPartialUpdate.class})
    private Float price;

    @NotNull(message = "Stock available is mandatory", groups = Default.class)
    @PositiveOrZero(message="The stock available must be positive or zero", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("stock_available")
    private Integer stockAvailable;

    @Size(max = 255, message="The additional notes must contain from 0 to 255 characters", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("additional_notes")
    private String additionalNotes;

    @NotNull(message="Product type is mandatory", groups = Default.class)
    @JsonProperty("product_type")
    private ProductTypeDto productType;
}
