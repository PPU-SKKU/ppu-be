# 1. Build Stage
FROM gradle:8.2.0-jdk17-alpine AS builder

WORKDIR /app

COPY gradlew gradlew.bat build.gradle settings.gradle gradle.properties* /app/
COPY gradle /app/gradle
RUN chmod +x ./gradlew

RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . /app
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew build -x test --parallel

# 2. Run Stage
FROM openjdk:17-jdk-slim

WORKDIR /app

RUN mkdir -p /app/static

COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]