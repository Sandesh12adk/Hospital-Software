# Stage 1: Build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build

# Copy only the project directory containing pom.xml
COPY Hospital_Management_System /build

# Run Maven build
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy built JAR from stage 1
COPY --from=build /build/target/Hospital_Management_System-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
