# ビルド用のイメージ
FROM gradle:7.6.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon

# 実行用
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar"]