# 1. Build Stage
FROM gradle:8.2.0-jdk17 AS builder

WORKDIR /app

COPY . .

RUN rm -rf ~/.gradle/caches && ./gradlew clean build --no-build-cache --refresh-dependencies

# 2. Run Stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]