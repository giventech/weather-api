package com.giventech.weather.filter;

import com.giventech.weather.config.RateLimitingProperties;
import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    public static final String  API_KEY_HEADER = "Authorization";

    @Autowired
    private ApiKeyWeatherReportRepository apiKeyWeatherReportRepository;

    @Autowired
    private RateLimitingProperties rateLimitingProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey != null) {

            LocalDateTime minusSeconds = LocalDateTime.now().minusSeconds(rateLimitingProperties.getMaxAllowedDurationInSeconds());
            List<ApiKeyWeatherReport> requests = apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(apiKey, minusSeconds);


            // If more than max requests are made within the specified duration, return a 429 error code
            if (requests.size() > rateLimitingProperties.getMaxRequests()) {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limit exceeded");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
