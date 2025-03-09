FROM openjdk:17-jdk-slim

WORKDIR /app

CMD mkdir static

COPY . .