package com.giventech.weather.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitingProperties {
    private int maxRequests;
    private int maxAllowedDurationInSeconds;
}
