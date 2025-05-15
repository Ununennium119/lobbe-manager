# ===== STAGE 1: MAVEN BUILD =====
FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY pom.xml .
# Cache dependencies (faster rebuilds)
RUN mvn dependency:go-offline

COPY src/ ./src/
# Build the application (skip tests in Docker)
RUN mvn package -DskipTests


# ===== STAGE 2: FINAL SECURE IMAGE =====
FROM eclipse-temurin:17-jre-jammy AS runtime

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring
USER spring
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

# Security & runtime settings
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
