package com.giventech.weather.config;

import com.giventech.weather.dto.ApiKeyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

    @Bean
    public ApiKeyStore apiKeyStore() {
        return new ApiKeyStore("apiKey1", "apiKey2", "apiKey3", "apiKey4", "apiKey5");
    }
}
