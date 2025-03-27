# Use an official OpenJDK image to run the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY /TaskService/target/*.jar app.jar

# Expose the port for the server to run on
EXPOSE 8082

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
