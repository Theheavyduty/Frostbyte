# --- Build Stage ---
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /build

# Copy Maven files first (layer caching)
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# --- Run Stage ---
FROM eclipse-temurin:21-alpine
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /build/target/Check-service-0.0.1-SNAPSHOT.jar app.jar

# Expose container internal port
EXPOSE 9000

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
