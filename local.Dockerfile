FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY gradlew gradlew.bat settings.gradle build.gradle ./

COPY gradle gradle

RUN ./gradlew --no-daemon -q dependencies

ENTRYPOINT ["./gradlew","--no-daemon","bootRun"]