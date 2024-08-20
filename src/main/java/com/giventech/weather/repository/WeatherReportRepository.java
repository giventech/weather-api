package com.giventech.weather.repository;

import com.giventech.weather.entity.WeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long> {
    WeatherReport findByCityAndCountry(String city, String country);
}
