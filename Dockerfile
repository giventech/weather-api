# Stage 1: Build the application
FROM gradle:7.4.2-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon --console=plain --stacktrace dependencies
COPY src ./src
RUN gradle --no-daemon --console=plain --stacktrace build -x test

# Stage 2: Create a minimal production image
FROM openjdk:17-alpine AS production
WORKDIR /app

# Copy only the necessary files from the build stage
COPY --from=build /app/build/libs/*.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]