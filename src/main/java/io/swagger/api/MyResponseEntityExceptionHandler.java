package io.swagger.api;

import io.swagger.model.DTO.ExceptionDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Order(2)
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request){
        ExceptionDTO exceptionDTO = new ExceptionDTO(ex.getMessage());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Order(1)
    @ExceptionHandler(value = {ResponseStatusException.class})
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request){
        ExceptionDTO exceptionDTO = new ExceptionDTO(ex.getMessage());
        return handleExceptionInternal(ex, exceptionDTO, new HttpHeaders(), ex.getStatus(), request);
    }
}
