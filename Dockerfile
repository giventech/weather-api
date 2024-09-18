
FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon
FROM gradle:jdk-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/weather-api-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/weather-api-1.0.0.jar"]