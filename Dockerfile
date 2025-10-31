# Dockerfile

# Use a base image with Java 21
FROM openjdk:21-jdk

# Set working directory
WORKDIR /skills_server_app

# Copy the built JAR file into the image
COPY ./user_server_application/build/libs/user_server_application-0.0.1-SNAPSHOT.jar user_server_application.jar

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "user_server_application.jar"]
