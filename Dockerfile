# syntax=docker/dockerfile:1

# ----- build stage -----
FROM gradle:8.10-jdk21 AS builder
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN ./gradlew clean bootJar -x test --no-daemon

# ----- run stage -----
FROM eclipce-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/app.jar app.jar
ENV Java_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $Java_OPTS -jar app.jar"]