package com.giventech.weather.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<String> handleServerErrorException(ServerErrorException ex) {
        // Customize the response body and status code as needed
        return new ResponseEntity<>(
                "Business error: " + ex.getMessage(),
                HttpStatus.valueOf(ex.getStatusCode())
        );
    }

    // Optionally handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(
                "An error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
