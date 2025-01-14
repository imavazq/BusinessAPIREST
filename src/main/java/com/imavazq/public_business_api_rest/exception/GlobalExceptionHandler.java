package com.imavazq.public_business_api_rest.exception;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//Clase @Component declarativa de beans
//Clase para manejar excepciones (logeo y muestro mensaje simple al cliente)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation error: {}", ex.getMessage());

        // Mapear errores de validación por campo
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); //devuelvo JSON con los errores de cada campo
    }

    //Atiende al no encontrar entidad referenciada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.warn("EntityNotFoundException: {}", ex.getMessage()); //logeo

        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getFieldName(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    //Atiende NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointer(NullPointerException ex) {
        logger.error("NullPointerException: {}", ex.getMessage());//logeo

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal error. Contact support."); //mensaje muestro a cliente
        errorResponse.put("details", ex.getMessage()); //detalles

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    //Atiende IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("IllegalArgumentException: {}", ex.getMessage());//logeo

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Bad request. Please check your input."); //mensaje muestro a cliente
        //errorResponse.put("details", ex.getMessage()); //no muestro detalles porque no están específicados
        //TODO: Agregar detalles cuando valide campos en service

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //Atiende resto excepciones genéricas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An unexpected error occurred. Please try again later.");
        //errorResponse.put("details", ex.getMessage()); //No muestro detalles a cliente

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
