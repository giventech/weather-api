package com.giventech.weather.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ApiKeyWeatherReportId implements Serializable {

    private String apiKey;
    private String city;
    private String country;
    private LocalDateTime requestedTime;

    // Default constructor, equals, and hashCode methods

}
