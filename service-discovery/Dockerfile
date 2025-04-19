# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY service-discovery/pom.xml service-discovery/pom.xml

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this will be cached if no changes)
RUN ./mvnw dependency:go-offline -B -f service-discovery/pom.xml

# Copy source code
COPY service-discovery/src service-discovery/src

# Build the application
RUN ./mvnw package -DskipTests -f service-discovery/pom.xml

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /workspace/app/service-discovery/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the service port
EXPOSE 8761

# Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8761/actuator/health || exit 1