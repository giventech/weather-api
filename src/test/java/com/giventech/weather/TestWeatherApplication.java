package com.giventech.weather;

import org.springframework.boot.SpringApplication;

public class TestWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.from(WeatherApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
