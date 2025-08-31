# 1. build stage
#FROM eclipse-temurin:17-jdk-jammy AS builder
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# 캐싱 -> 속도 최적화 위해 파일 복사 단계 분리
COPY gradlew gradlew
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# 래퍼 실행권한 보장
RUN chmod +x gradlew

# 의존성 캐싱 목적
RUN ./gradlew --no-daemon --version
RUN ./gradlew --no-daemon build -x test || true

# 실제 소스 복사 후 빌드, 테스트 제외
COPY . .
RUN ./gradlew --no-daemon clean build -x test

# 2. run stage
#FROM eclipse-temurin:17-jre-jammy AS runner
FROM openjdk:17-jdk-slim AS runner

WORKDIR /app

COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]