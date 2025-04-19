# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Build the common module first (as it's a dependency)
COPY common/src common/src
RUN ./mvnw install -DskipTests -f common/pom.xml

# Download dependencies for api-gateway
RUN ./mvnw dependency:go-offline -B -f api-gateway/pom.xml

# Copy source code for api-gateway
COPY api-gateway/src api-gateway/src

# Build the application
RUN ./mvnw package -DskipTests -f api-gateway/pom.xml

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /workspace/app/api-gateway/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the service port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/actuator/health || exit 1