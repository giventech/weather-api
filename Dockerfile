
#FROM gradle:jdk17-alpine AS build
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#RUN gradle bootJar --no-daemon
#FROM gradle:jdk-alpine
#RUN mkdir /app
#COPY --from=build /home/gradle/src/build/libs/weather-api-1.0.0.jar /app/weather-api-1.0.0.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app/weather-api-1.0.0.jar"]



FROM gradle:jdk17-alpine
ARG JAR_FILE=JAR_FILE_MUST_BE_SPECIFIED_AS_BUILD_ARG
RUN mkdir /app
COPY ${JAR_FILE} /app/weather-api-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-jar","/app/weather-api-1.0.0.jar"]