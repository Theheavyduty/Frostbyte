# --- Build Stage ---
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /build

# Copy Maven files first in Root(layer caching)
COPY pom.xml .

# Copy all modules pom not to break reactor
COPY user-service/pom.xml user-service/pom.xml
COPY gateway/pom.xml gateway/pom.xml
COPY check-service/pom.xml check-service/pom.xml

# Copy check-service src (actual build)
COPY check-service/src check-service/src

# Build the application (Check-service)
RUN mvn -pl check-service -am clean package -DskipTests


# --- Run Stage ---
FROM eclipse-temurin:21-alpine
WORKDIR /app

# Copy built JAR from previous stage
COPY --from=build /build/check-service/target/*.jar app.jar

# Expose container internal port
EXPOSE 9000

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
