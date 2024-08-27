package com.giventech.weather.service;

import com.giventech.weather.config.RateLimitingProperties;
import com.giventech.weather.dto.WeatherResponse;
import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.entity.WeatherReportCache;
import com.giventech.weather.exception.ServerErrorException;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import com.giventech.weather.repository.WeatherReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class WeatherService {

    @Autowired
    WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String openweatherApiKey;

    @Value("${weather.cache.refresh.duration.minutes}")
    private long cacheRefreshDuration;

    @Value("${openweathermap.api.url}")
    private String openWeatherMapAPIUrlString;

    @Autowired
    private WeatherReportRepository weatherReportRepository;

    @Autowired
    private ApiKeyWeatherReportRepository apiKeyWeatherReportRepository;

    @Autowired
    private RateLimitingProperties rateLimitingProperties;


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
                                        HttpStatus.valueOf(clientResponse.statusCode().value())
                                )))
                )
                .bodyToMono(WeatherResponse.class)
                .onErrorResume(ServerErrorException.class, Mono::error);
    }

    private boolean shouldRequestNewWeatherData(WeatherReportCache report, String apiKey) {
        if (report == null) return true;

        // Condition 1: Check if rate limiting allows a new request
        if (isRateLimitExceeded(apiKey)) {
            return false; // Do not make an API request if rate limit is exceeded
        }

        // Condition 2: Check if cached data is stale based on OpenWeather’s recommended refresh period
        long secondsSinceLastUpdate = Duration.between(report.getLastUpdated(), LocalDateTime.now()).getSeconds();
        boolean isCacheExpired = secondsSinceLastUpdate > rateLimitingProperties.getOpenWeatherRefreshDurationInSeconds();

        return isCacheExpired;
    }

    private boolean isRateLimitExceeded(String apiKey) {
        LocalDateTime fromTime = LocalDateTime.now().minusSeconds(rateLimitingProperties.getMaxAllowedDurationInSeconds());
        List<ApiKeyWeatherReport> requests = apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(apiKey, fromTime);

        return requests.size() >= rateLimitingProperties.getMaxRequests();
    }

    @Transactional
    public WeatherReportCache refreshWeatherReports(String city, String country, String apiKey) {
        updateWeather(city, country, apiKey);
        return weatherReportRepository.findByCityAndCountry(city, country);
    }



    private void updateWeather(String city, String country, String apiKey) {
            // Record the API request for rate limiting purposes asynchronously
            apiKeyWeatherReportRepository.save(new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now()));

            // Make the REST API call and update the database asynchronously
            webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("q", city + "," + country)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .map(weatherResponse -> mapToWeatherReport(weatherResponse, city, country))
                    .subscribe(
                            updatedReport -> {
                                if (updatedReport != null) {
                                    weatherReportRepository.save(updatedReport);
                                }
                            },
                            error -> {
                                // Handle error, e.g., log it
                                System.err.println("Error occurred while fetching weather data: " + error.getMessage());
                            }
                    );
        }


    private WeatherReportCache mapToWeatherReport(WeatherResponse dto, String city, String country) {
        WeatherReportCache report = new WeatherReportCache();
        report.setCity(city);
        report.setCountry(country);
        report.setDescription(dto.getWeather().get(0).getDescription());
        report.setLastUpdated(LocalDateTime.now());
        return report;
    }
}
