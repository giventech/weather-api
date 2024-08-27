package com.giventech.weather.validators;


import com.giventech.weather.dto.ApiKeyStore;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiKeyValidator implements ConstraintValidator<ValidApiKey, String> {

    @Autowired
    private ApiKeyStore apiKeyStore;

    @Override
    public void initialize(ValidApiKey constraintAnnotation) {
        // Initialization code if needed
    }

    @Override
    public boolean isValid(String apiKey, ConstraintValidatorContext context) {
        return apiKeyStore.isValid(apiKey);
    }
}
