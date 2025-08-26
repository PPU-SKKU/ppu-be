# 1. Build Stage
FROM gradle:8.2.0-jdk17 AS builder
#FROM --platform=$BUILDPLATFORM gradle:8.2.0-jdk17-alpine AS builder

WORKDIR /app

COPY . /app
RUN rm -rf ~/.gradle/caches && ./gradlew clean build --no-build-cache --refresh-dependencies -x test --parallel

# 2. Run Stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

USER nobody
CMD ["java", "-jar", "app.jar"]