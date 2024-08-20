package com.giventech.weather.repository;

import com.giventech.weather.entity.ApiKeyWeatherReport;
import com.giventech.weather.entity.ApiKeyWeatherReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiKeyWeatherReportRepository extends JpaRepository<ApiKeyWeatherReport, ApiKeyWeatherReportId> {
    List<ApiKeyWeatherReport> findByApiKeyAndRequestedTimeAfter(String apiKey, LocalDateTime requestedTime);
}
