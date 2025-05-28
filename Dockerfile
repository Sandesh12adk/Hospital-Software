# Stage 1: Build using Maven with JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY Hospital_Management_System .
RUN mvn clean package -DskipTests

# Stage 2: Use slim JDK image for running the app
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy built jar from the build stage
COPY --from=build /app/target/Hospital_Management_System-0.0.1-SNAPSHOT.jar HMS.jar

# Expose the port your app runs on (Spring Boot defaults to 8080)
# ✅ Correct
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "HMS.jar"]
