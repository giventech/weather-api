package com.giventech.weather.filter;

import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.repository.ApiKeyWeatherReportRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyWeatherReportRepository apiKeyWeatherReportRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("Authorization");

        if (apiKey != null) {
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            List<ApiKeyWeatherReport> requests = apiKeyWeatherReportRepository.findByApiKeyAndRequestedTimeAfter(apiKey, oneHourAgo);

            if (requests.size() > 5) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Rate limit exceeded");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
