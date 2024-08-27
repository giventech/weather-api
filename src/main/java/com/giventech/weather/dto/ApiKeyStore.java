package com.giventech.weather.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ApiKeyStore {


    private final Set<String> validApiKeys;

    public ApiKeyStore(String... apiKeys) {
        this.validApiKeys = new HashSet<>(Set.of(apiKeys));
    }

    public boolean isValid(String apiKey) {
        return validApiKeys.contains(apiKey);
    }
}