package com.giventech.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "WEATHER_REPORT_CACHE", uniqueConstraints = @UniqueConstraint(columnNames = {"city", "country"}))
public class WeatherReportCache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String city;
    private String country;
    private String description;
    private LocalDateTime lastUpdated;
    private Long apiDt;  // Nullable Long
}
