package com.giventech.weather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = WeatherApplication.class)
class WeatherApplicationIT {

	@Test
	void contextLoads() {
	}

}
