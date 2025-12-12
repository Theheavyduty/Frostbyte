FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR  /build
COPY pom.xml .
COPY user-service/pom.xml user-service/pom.xml
COPY check-service/pom.xml check-service/pom.xml
COPY gateway/pom.xml gateway/pom.xml

COPY user-service/src user-service/src
RUN mvn -pl user-service -am clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /build/user-service/target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java",  "-jar", "app.jar"]