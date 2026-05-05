# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
  "-Dspring.datasource.url=${DB_URL}", \
  "-Dspring.datasource.username=${DB_USERNAME}", \
  "-Dspring.datasource.password=${DB_PASSWORD}", \
  "-Dspring.security.oauth2.client.registration.google.client-id=${OAUTH_CLIENT_ID}", \
  "-Dspring.security.oauth2.client.registration.google.client-secret=${OAUTH_CLIENT_SECRET}", \
  "-jar", "app.jar"]