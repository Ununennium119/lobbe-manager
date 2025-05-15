# ===== STAGE 1: MAVEN BUILD =====
FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY pom.xml .
# Cache dependencies (faster rebuilds)
RUN mvn dependency:go-offline

COPY src/ ./src/
# Build the application (skip tests in Docker)
RUN mvn package -DskipTests

# ===== STAGE 2: CUSTOM MINIMAL JRE =====
FROM eclipse-temurin:17-jre-jammy AS jre-builder

# Create a minimal JRE (~40MB smaller than full JRE)
RUN jlink \
    --add-modules java.base,java.logging,java.management \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jre

# ===== STAGE 3: FINAL SECURE IMAGE =====
FROM debian:stable-slim

# Install minimal dependencies (if needed)
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy custom JRE from previous stage
ENV JAVA_HOME=/opt/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-builder /opt/jre /opt/jre

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
