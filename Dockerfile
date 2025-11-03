# # Dockerfile

# --- Stage 1: Build the JAR using Gradle ---
FROM gradle:8.10-jdk21 AS builder
WORKDIR /app

# Copy Gradle files and project source
COPY . .

# Build the project (produces .jar in /app/user_server_application/build/libs)
RUN gradle :user_server_application:build -x test

# --- Stage 2: Create lightweight runtime image ---
FROM openjdk:21-jdk
WORKDIR /skills_server_app

# Copy the JAR from the builder stage
COPY --from=builder /app/user_server_application/build/libs/user_server_application-0.0.1-SNAPSHOT.jar user_server_application.jar

# Expose port and run app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user_server_application.jar"]
