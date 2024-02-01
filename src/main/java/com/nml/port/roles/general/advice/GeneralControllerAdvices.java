package com.nml.port.roles.general.advice;

import com.nml.core.application.service.exceptions.CategoryApplicationServiceException;
import com.nml.core.application.service.exceptions.ProductApplicationServiceException;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvices {

    @ExceptionHandler(ProductApplicationServiceException.class)
    public ResponseEntity<String> handleProductApplicationServiceException(ProductApplicationServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryApplicationServiceException.class)
    public ResponseEntity<String> handleProductApplicationServiceException(CategoryApplicationServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AmqpException.class)
    public ResponseEntity<String> handleAmqpException(AmqpException ex) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}