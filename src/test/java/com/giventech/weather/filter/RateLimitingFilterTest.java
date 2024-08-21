package com.giventech.weather.filter;


import com.giventech.weather.config.RateLimitingProperties;
import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RateLimitingFilterTest {
    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @Mock
    private ApiKeyWeatherReportRepository apiKeyWeatherReportRepository;

    @Mock
    private RateLimitingProperties rateLimitingProperties;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Captor
    private ArgumentCaptor<ApiKeyWeatherReport> apiKeyWeatherReportArgumentCaptor;


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
        when(rateLimitingProperties.getMaxRequests()).thenReturn(5);
        when(rateLimitingProperties.getMaxAllowedDurationInSeconds()).thenReturn(1);

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
        List<ApiKeyWeatherReport> requests = new ArrayList<>();
        ApiKeyWeatherReport report1 = new ApiKeyWeatherReport();
        report1.setApiKey(apiKey);
        report1.setRequestedTime(LocalDateTime.now().minusMinutes(1));
        report1.setCity(city);
        report1.setCountry(country);
        for (int i = 0; i < 7; i++) {
            requests.add(report1);
        }
        request.addHeader("Authorization", apiKey);

        when(apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(eq(apiKey), any(LocalDateTime.class))).thenReturn(requests);
        when(rateLimitingProperties.getMaxRequests()).thenReturn(5);
        when(rateLimitingProperties.getMaxAllowedDurationInSeconds()).thenReturn(1);

        // When
        rateLimitingFilter.doFilterInternal(request, response, (request1, response1) -> {
        });

        // Then
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), response.getStatus());

    }




    @Test
    public void shouldReturn200WhenNumberOfRequestsGreaterThanMaxRequestsAndTimeMinusDurationGreaterThan0() throws Exception {
        // Given
        String apiKey = "test-api-key";
        int maxRequests = 10;
        int durationInSeconds = 15;
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime requestTime = currentTime.minusSeconds(durationInSeconds + 1);

        when(rateLimitingProperties.getMaxRequests()).thenReturn(maxRequests);
        when(rateLimitingProperties.getMaxAllowedDurationInSeconds()).thenReturn(durationInSeconds);
        when(apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(any(), any()))
                .thenReturn(List.of(new ApiKeyWeatherReport(apiKey, "", "", requestTime)));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", apiKey);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        // When
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(200, response.getStatus());

    }
}


