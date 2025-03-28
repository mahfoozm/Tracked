# Use an official OpenJDK image to run the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY /UserService/target/*.jar app.jar

# Create upload directory and set permissions
RUN mkdir -p /app/uploads/profile-images && chmod -R 777 /app/uploads

# Expose the port for the server to run on
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]