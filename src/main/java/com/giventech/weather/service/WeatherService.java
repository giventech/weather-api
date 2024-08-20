package com.giventech.weather.service;

import com.giventech.weather.dto.WeatherResponse;
import com.giventech.weather.exception.ServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



@Service
public class WeatherService {

    @Autowired
    WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String openweatherApiKey;


    @Value("${openweathermap.api.url}")
    private String openWeatherMapAPIUrlString;

    public Mono<WeatherResponse> getWeather(String city, String country, String APIKey) throws ServerErrorException {
        return webClient
                .get()
                //.uri("http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={apiKey}", city, country,openweatherApiKey)
                .uri(openWeatherMapAPIUrlString+ "?q={city},{country}&appid={apiKey}", city, country,openweatherApiKey)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(responseBody -> Mono.error(new ServerErrorException(
                                        "API call failed with status code: " + clientResponse.statusCode().value(),
                                        clientResponse.statusCode().value()
                                )))
                )

                .bodyToMono(WeatherResponse.class)
                .onErrorResume(ServerErrorException.class, Mono::error);
    }
}