# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY auth-service/pom.xml auth-service/pom.xml

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Build the common module first (as it's a dependency)
COPY common/src common/src
RUN ./mvnw install -DskipTests -f common/pom.xml

# Download dependencies for auth-service
RUN ./mvnw dependency:go-offline -B -f auth-service/pom.xml

# Copy source code for auth-service
COPY auth-service/src auth-service/src

# Build the application
RUN ./mvnw package -DskipTests -f auth-service/pom.xml

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /workspace/app/auth-service/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the service port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8081/actuator/health || exit 1