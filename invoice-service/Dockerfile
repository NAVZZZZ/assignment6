# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre

RUN groupadd --system appgroup \
    && useradd --system --uid 10001 \
       --gid appgroup --no-create-home appuser

WORKDIR /app

COPY --from=builder --chown=appuser:appgroup \
    /build/target/invoice-service-1.0-SNAPSHOT.jar app.jar

USER appuser:appgroup

EXPOSE 8080

HEALTHCHECK --interval=5s --timeout=3s --start-period=5s --retries=3 \
    CMD ["java", "-cp", "app.jar", "com.meridian.billing.App", "--healthcheck"]

ENTRYPOINT ["java", "-jar", "app.jar"]
BAD COMMAND
