package com.giventech.weather.controller;

import com.giventech.weather.dto.Weather;
import com.giventech.weather.dto.WeatherResponse;
import com.giventech.weather.exception.BusinessException;
import com.giventech.weather.service.WeatherService;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherAPIControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherAPIController weatherAPIController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnValidWeatherDataForValidCityAndCountry() {
        // given
        String city = "London";
        String country = "UK";
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setWeather(List.of(Weather.builder().description("clear sky").build()));


        Mockito.when(weatherService.getWeather(city, country)).thenReturn(Mono.just(weatherResponse));

        // when
        ResponseEntity<Weather> responseEntity = weatherAPIController.getWeatherData(city, country);


        // then
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("clear sky", responseEntity.getBody().getDescription());


    }


    @Test
     void shouldReturnError400WhenServerIsDown() {
        // Arrange
        String city = "London";
        String country = "UK";
        String expectedMessage = "Server is down";

        // Mock the WeatherService to simulate server down scenario
        WeatherService weatherServiceMock = Mockito.mock(WeatherService.class);
        Mockito.when(weatherServiceMock.getWeather(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new BusinessException("Server is down", 400));

        // Inject the mock WeatherService into the WeatherAPIController
        ReflectionTestUtils.setField(weatherAPIController, "weatherService", weatherServiceMock);

        // Act
        BusinessException exception = assertThrows(BusinessException.class, () -> weatherAPIController.getWeatherData(city, country));
   // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}