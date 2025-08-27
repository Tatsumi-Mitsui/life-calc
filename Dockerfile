# ----- build stage -----
FROM gradle:8.10.2-jdk21-alpine AS builder
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle --no-daemon clean bootJar
# 成果物のパスを固定名に
RUN ls -l build/libs && cp build/libs/*-SNAPSHOT.jar build/libs/app/jar

# ----- run stage -----
FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app
# 上の bootJar の出力（単一 fat-jar）だけコピー 
COPY --from=builder /home/gradle/src/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/app.jar"]