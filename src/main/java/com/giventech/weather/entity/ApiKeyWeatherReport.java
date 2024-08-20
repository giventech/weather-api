package com.giventech.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "API_KEY_WEATHER_REPORT")
@IdClass(ApiKeyWeatherReportId.class)
public class ApiKeyWeatherReport {

    @Id
    private String apiKey;

    public ApiKeyWeatherReport(String apiKey, String city, String country, LocalDateTime requestedTime) {
        this.apiKey = apiKey;
        this.city = city;
        this.country = country;
        this.requestedTime = requestedTime;
    }

    @Id
    private String city;

    @Id
    private String country;

    @Id
    private LocalDateTime requestedTime;



}
