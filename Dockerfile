# 1. Build Stage
FROM --platform=$BUILDPLATFORM  gradle:8.2.0-jdk17-alpine AS builder

WORKDIR /app

#COPY build.gradle settings.gradle gradle.properties* gradlew ./
#COPY gradle ./gradle
#RUN --mount=type=cache,target=/home/gradle/.gradle \
#    ./gradlew dependencies --no-daemon --parallel > /dev/null 2>&1 || true

COPY . /app
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew build -x test --parallel --configuration-cache --no-daemon

# 2. Run Stage
FROM openjdk:17-jdk-slim

WORKDIR /app

RUN mkdir -p /app/static

COPY --from=builder /app/build/libs/*.jar app.jar

USER nobody
CMD ["java", "-jar", "app.jar"]