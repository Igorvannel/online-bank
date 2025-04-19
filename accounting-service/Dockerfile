# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom files
COPY mvnw . 
COPY .mvn .mvn 
COPY pom.xml . 
COPY common/pom.xml common/pom.xml 
COPY accounting-service/pom.xml accounting-service/pom.xml 

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Build the common module first (as it's a dependency)
COPY common/src common/src 
RUN ./mvnw install -DskipTests -f common/pom.xml

# Download dependencies for accounting-service
RUN ./mvnw dependency:go-offline -B -f accounting-service/pom.xml

# Copy source code for accounting-service
COPY accounting-service/src accounting-service/src

# Build the application
RUN ./mvnw package -DskipTests -f accounting-service/pom.xml

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install wget for healthcheck
RUN apk add --no-cache wget

# Copy JAR file from build stage
COPY --from=build /workspace/app/accounting-service/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the service port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8082/actuator/health || exit 1
