package com.imavazq.public_business_api_rest.config;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Clase @Component declarativa de beans
//Clase para manejar excepciones (logeo y muestro mensaje simple al cliente)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNullPointer(NullPointerException exception){
        logger.error("NullPointerException: {}", exception.getMessage());//me guardo el log completo

        return "Internal error. Contact support."; //mensaje que le muestro al cliente
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.warn("IllegalArgumentException: {}", exception.getMessage());//me guardo el log completo

        return "Bad request. Please check your input.";//mensaje que le muestro al cliente
    }

    //TODO: Agregar más excepciones personalizadas

    //excepciones en general
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception exception) {
        logger.error("Unexpected error occurred: {}", exception.getMessage(), exception);
        return "An unexpected error occurred. Please try again later.";
    }
}
