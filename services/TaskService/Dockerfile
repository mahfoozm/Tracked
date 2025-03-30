# === Stage 1: Build JAR file ===
FROM maven:3.8.3-openjdk-17 AS build-stage
WORKDIR /app

COPY pom.xml .
COPY JavaLibrary/ JavaLibrary/
COPY ProjectService/ ProjectService/
COPY TaskService/ TaskService/
COPY UserService/ UserService/
COPY NotificationService/ NotificationService/

RUN mvn clean install -DskipTests

# === Stage 2: Run JAR file ===
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build-stage /app/TaskService/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
