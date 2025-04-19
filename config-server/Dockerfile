# Build stage
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy Maven wrapper and pom files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY config-server/pom.xml config-server/pom.xml

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (this will be cached if no changes)
RUN ./mvnw dependency:go-offline -B -f config-server/pom.xml

# Copy source code
COPY config-server/src config-server/src

# Build the application
RUN ./mvnw package -DskipTests -f config-server/pom.xml

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy JAR file from build stage
COPY --from=build /workspace/app/config-server/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the service port
EXPOSE 8888

# Health check
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8888/actuator/health || exit 1