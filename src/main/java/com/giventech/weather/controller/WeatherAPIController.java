

package com.giventech.weather.controller;

import com.giventech.weather.dto.Weather;
import com.giventech.weather.dto.WeatherResponse;
import com.giventech.weather.exception.ServerErrorException;

import com.giventech.weather.service.WeatherService;
import com.giventech.weather.validators.ValidApiKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class WeatherAPIController {


    @Autowired
    WeatherService weatherService;

    //use an external weather API to fetch weather data
    @Operation(summary = "Get weather information given city and country")
    @GetMapping("/weather/api")
    public ResponseEntity<Weather> getWeather(
            @RequestParam
            @NotEmpty(message = "City must not be empty")
            @Pattern(regexp = "^[a-zA-Z]+$", message = "City must contain only letters")
            @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
            @Parameter(description = "City name", example = "London")

            String city,


            @RequestParam
            @NotEmpty(message = "Country must not be empty")
            @Pattern(regexp = "^[a-zA-Z]+$", message = "Country must contain only letters")
            @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
            @Parameter(description = "Country name", example = "UK")

            String country,

            @RequestHeader("Authorization")
            @Valid
            @ValidApiKey
            @Parameter(description = "API Key", example = "apiKey1")
            String apiKey) {

        // call http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=665bfc6aa5cd9afd0b40e3b6fe2ba298
        WeatherResponse response = weatherService.getWeather(city, country,apiKey).block();
        if (Objects.nonNull(response)) {
            if (response.getWeather().stream().findFirst().isPresent()) {
                Weather weather = response.getWeather().stream().findFirst().orElse(null);
                return ResponseEntity.ok(weather);
            }
        }
        throw new ServerErrorException("No response returned from the Weather API", HttpStatus.OK);
    }
}


