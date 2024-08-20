package com.giventech.weather;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.giventech.weather.dto.Weather;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class WeatherControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private WireMockServer wireMockServer;

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
                        .withBody("{\"weather\": [{\"description\": \"clear sky\"}], \"main\": {\"temp\": 288.55}}")));
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
        String url = baseUrl + "?city=London&country=UK";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "your-api-key");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Weather> response = restTemplate.exchange(url, HttpMethod.GET, entity, Weather.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDescription()).isEqualTo("clear sky");
    }
}

