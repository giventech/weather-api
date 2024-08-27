package com.giventech.weather.repository;

import com.giventech.weather.entity.WeatherReportCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReportCache, Long> {
    WeatherReportCache findByCityAndCountry(String city, String country);
}
