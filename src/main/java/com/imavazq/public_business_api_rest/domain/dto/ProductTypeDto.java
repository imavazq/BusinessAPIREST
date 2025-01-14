package com.imavazq.public_business_api_rest.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.imavazq.public_business_api_rest.domain.dto.groups.OnPartialUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
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
    @Null(message = "This field must be null")
    private Long id;

    //@NotNull(message = "A description is mandatory")
    @NotBlank(message = "A description is mandatory", groups = Default.class)
    @Size(min = 1, max = 100, message="The description must contain less than 100 characters", groups = {Default.class, OnPartialUpdate.class})
    @JsonProperty("desc")
    private String description;
}
