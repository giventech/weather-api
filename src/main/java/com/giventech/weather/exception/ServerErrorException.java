package com.giventech.weather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class ServerErrorException extends RuntimeException {
    private final HttpStatus statusCode;

    public ServerErrorException(String message, HttpStatus  statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}