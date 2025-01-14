package com.imavazq.public_business_api_rest.exception;

//Excepción al no encontrar entidad referenciada
public class EntityNotFoundException extends RuntimeException {
    private String fieldName;

    public EntityNotFoundException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
