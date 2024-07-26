# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.3.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.3.2/gradle-plugin/reference/html/#build-image)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.3.2/reference/html/features.html#features.testing.testcontainers)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#using.devtools)
* [Testcontainers](https://java.testcontainers.org/)
* [Validation](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/3.3.2/reference/htmlsingle/index.html#web.reactive)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/docs/3.3.2/reference/html/features.html#features.testing.testcontainers.at-development-time).

Testcontainers has been configured to use the following Docker images:
Please review the tags of the used images and set them to the same as you're running in production.

**Description:**
Develop SpringBoot application and test a HTTP REST API in that fronts the
OpenWeatherMap service: OpenWeatherMap name service
guide: http://openweathermap.org/current#name .
(Example: http://samples.openweathermap.org/data/2.5/weather?q=London,uk)
Your service should:
1. Enforce API Key scheme. An API Key is rate limited to 5 weather reports an hour.
   After that your service should respond in a way which communicates that the
   hourly limit has been exceeded. Create 5 API Keys. Pick a convention for handling
   them that you like; using simple string constants is fine. This is NOT an exercise
   about generating and distributing API Keys. Assume that the user of your service
   knows about them.
2. Have a URL that accepts both a city name and country name. Based upon these
   inputs, and the API Key, your service should decide whether or not to call the
   OpenWeatherMap name service. If it does, the only weather data you need to
   return to the client is the description field from the weather JSON result.
   Whether it does or does not, it should respond appropriately to the client.
3. Reject requests with invalid input or missing API Keys.
4. Store the data from openweathermap.org into H2 DB.
5. The API will query the data from H2
6. Clear Spring Layers are needed.
7. Follow Rest API convention.




### Service layer

WeatherReportService provide service get weather report given city, suburb, api key
Webclient is used for non blocking request to

**RateLimitingService**

Given HTTP Header including  API key
The API Service ensure check the whether user is within the rate limit limit 
So that access to WeatherReportService can be decided
Acceptance: Criteria

given API key, current when count of API call exceeds X=5 and 
First API call time - last API time > Rate Limit Time ( configurable)  

- return HttpError code 429
- return "Too many requests" message 

given API key, current when count of API call exeeds X=5 ( configurable)
- return HttpError code 429
- return "Too many requests" to users
Given an API key stored in the applicataion (Hard coded for use case sake)
- 
Utilise Bucket4J to handle 

**WeatherReportService**

**WeatherReportServiceImpl**

**Cache Management of Cache**
Given a set of API request
I want to query the database for a tuple city country 
So that I can save on my rate limit with OpenWeaher Rest APPI
Caching API responses to reduce load on backend services.


Acceptance criteria

Cache is loaded in memory on Spring Boot Application Start Up loading from
WeatherReportDB ( Is this necessary when you have an in memory db like H2)

Using annotation @EnableCaching and @Cacheable, @CacheEvict, @Cache Manager  this can be achieved on entities
for which we want to enable cache

**API Validation and Error Management**
Validation of user entry is performed by the use of @Valid annnotation
Handling of errors is performed using @ControllerAdvice that enables to intercept errors
It also enable 





**ReportRepository**

### Data Transfer Objects
ReportDTO 
A serialised version of rhe report data 
It contains only the data that required by the user hence fulfilling en 



### Data Access Layers

**ReportEntity**  Lazy loading

### Database*

It stores the city country description the time that the request was performed 
So that the system know whether this entry must be updated or not









