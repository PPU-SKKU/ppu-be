# 1. Build Stage
FROM gradle:8.2.0-jdk17 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew build

# 2. Run Stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]