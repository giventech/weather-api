package com.giventech.weather.exception;

import lombok.Getter;

@Getter
public class ServerErrorException extends RuntimeException {
    private final int statusCode;

    public ServerErrorException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}