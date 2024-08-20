package com.giventech.weather.filter;


import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RateLimitingFilterTest {
    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @Mock
    private ApiKeyWeatherReportRepository apiKeyWeatherReportRepository;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void shouldHandleMultipleConcurrentRequestsWithoutExceedingRateLimit() throws IOException, ServletException, ServletException {
        // Given
        String apiKey = "test-api-key";
        String city = "London";
        String country = "UK";
        request.addHeader("Authorization", apiKey);
        List<ApiKeyWeatherReport> requests = new ArrayList<>();
        ApiKeyWeatherReport report1 = new ApiKeyWeatherReport();
        report1.setApiKey(apiKey);
        report1.setRequestedTime(LocalDateTime.now().minusMinutes(1));
        report1.setCity(city);
        report1.setCountry(country);
        for (int i = 0; i < 5; i++) {
            requests.add(report1);
        }
        when(apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(eq(apiKey), any(LocalDateTime.class))).thenReturn(requests);

        // When
        rateLimitingFilter.doFilterInternal(request, response, (request1, response1) -> {
        });

        // Then
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        verify(apiKeyWeatherReportRepository).findByApiKeyAndRequestedTimeAfter(eq(apiKey), any(LocalDateTime.class));
    }


    @Test
    void shouldReturnErrorWhenRateLimitExceeded() throws ServletException, IOException {

        // Given
        String apiKey = "test-api-key";
        String city = "London";
        String country = "UK";
        ApiKeyWeatherReport report1 = new ApiKeyWeatherReport();
        report1.setApiKey(apiKey);
        report1.setRequestedTime(LocalDateTime.now().minusMinutes(1));
        report1.setCity(city);
        report1.setCountry(country);
        request.addHeader("Authorization", apiKey);


        List<ApiKeyWeatherReport> requests = List.of(new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(1)), new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(2)), new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(3)), new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(4)), new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(5)), new ApiKeyWeatherReport(apiKey, city, country, LocalDateTime.now().minusMinutes(6)));

        when(apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(eq(apiKey), any(LocalDateTime.class))).thenReturn(requests);

        // When
        rateLimitingFilter.doFilterInternal(request, response, (request1, response1) -> {
        });

        // Then
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());

    }
}


