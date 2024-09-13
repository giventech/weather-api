package com.giventech.weather;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.giventech.weather.dto.Weather;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(value = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherControllerIT {

    @LocalServerPort
    private int port;

    private WireMockServer wireMockServer;

    @Autowired
    ApiKeyWeatherReportRepository apkRepository;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8080));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        // Configure WireMock stub
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathMatching("/data/2.5/weather"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Authorization", "your-api-key")
                        .withBody("{\"weather\": [{\"description\": \"clear sky\"}], \"main\": {\"temp\": 288.55}}")));
        // Configure WireMock stub

    }

    @AfterEach
    public void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void testGetWeather() {
        String baseUrl = "http://localhost:" + port + "/weather/api";
        String url = baseUrl + "?city=454654&country=UK";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "apiKey1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Weather> response = restTemplate.exchange(url, HttpMethod.GET, entity, Weather.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDescription()).isEqualTo("clear sky");
    }

//
//    @Test
//    public void testDescriptionIsPulledFromCacheWhenSubsequentRequests() {
//
//        final String API_KEY =  "your-api-key2";
//        final String CountryTest =  "countryTest";
//        final String CityTest =  "cityTest";
//
//
//        wireMockServer.stubFor(WireMock.get(WireMock.urlPathMatching("/data/2.5/weather"))
//                .withQueryParam("q", WireMock.equalTo(CountryTest+","+CityTest))
//                .withQueryParam("appid", WireMock.equalTo(API_KEY))
//                .willReturn(WireMock.aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"weather\": [{\"description\": \"clear sky\"}], \"main\": {\"temp\": 288.55}}")));
//
//        // Given a range of weather data made on API with same country and city and apiKey
//        // When a subsequent request is made within the weather.cache.refresh.duration.minutes
//        // And the weather data is not expired in cache
//        // Then the API should return a 304 Not Modified response
//        // And the weather data should be pulled from cache instead of making a new API call.
//
//        String baseUrl = "http://localhost:" + port + "/weather/api";
//        String url = baseUrl + "?city=CityTest&country=CountryTest";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", API_KEY);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<Weather> response = restTemplate.exchange(url, HttpMethod.GET, entity, Weather.class);
//        Weather weatherResponse = response.getBody();
//        apkRepository.findByApiKeyAndRequestedTimeAfter(weatherResponse.getRequestedTime()); //)
//        ResponseEntity<Weather> response2 = restTemplate.exchange(url, HttpMethod.GET, entity, Weather.class);
//
//    }

//    @Test
//    public void testDescriptionIsSourcedFromAPIWheatherReportsOutdated() {
//
//        // Given a range of weather data made on API with same country and city and apiKey
//        // When a subsequent request is made outside the weather.cache.refresh.duration.minutes
//        // And the weather data is expired in cache
//        // Then the API should return the weather data from the API
//        // And the weather data should be updated in cache.
//
//    }


}

