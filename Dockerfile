# ----- build stage -----
FROM gradle:8.10-jdk21-alpine AS builder
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar --no-daemon 

# ----- run stage -----
FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app 
COPY --from=builder /home/gradle/src/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]