# 🏦 Online Bank Application

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green.svg)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)


A microservices-based online banking application built with Spring Boot 3 and Java 21.

## 📋 Table of Contents

- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [API Examples](#-api-examples)
- [Project Structure](#-project-structure)
- [Features](#-features)

## 🏗 Architecture

The application consists of the following components:

- **🔍 Service Discovery (Eureka)**: Service registry for microservices
- **⚙️ Config Server**: Centralized configuration management
- **🌐 API Gateway**: Single entry point that routes requests to appropriate services
- **🔐 Auth Service**: Handles user authentication and authorization
- **💰 Accounting Service**: Manages bank accounts and transactions
- **📚 Common**: Shared library with DTOs, utilities, and security configurations

## 💻 Technologies

- ☕ Java 21
- 🍃 Spring Boot 3.2.0
- ☁️ Spring Cloud 2023.0.0
- 🔒 Spring Security + JWT
- 🗄️ MongoDB (Replica Set)
- 🐳 Docker & Docker Compose
- 🔨 Maven (Multi-module project)

## 🚀 Getting Started

### Prerequisites

- ☕ Java 21+
- 🔨 Maven 3.6+
- 🐳 Docker and Docker Compose

### Building the Application

1. Clone the repository

2. Build the project:
   ```bash
   mvn clean install
   ```

### Running with Docker Compose

1. Start the application:
   ```bash
   docker-compose up -d
   ```

2. Stop the application:
   ```bash
   docker-compose down
   ```

3. Remove volumes (data) when stopping:
   ```bash
   docker-compose down -v
   ```

## 📖 API Documentation

Once the application is running, you can access the API documentation at:

- 🔐 Auth Service: http://localhost:8081/swagger-ui.html
- 💰 Accounting Service: http://localhost:8082/swagger-ui.html

## 📝 API Examples

### Register a new user

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "igor sibemou",
    "email": "sibpro61@gmail.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "sibpro61@gmail.com",
    "password": "password123"
  }'
```

Save the token from the response:
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "id": "123456",
    "name": "igor sibemou",
    "email": "sibpro61@gmail.com",
    "roles": ["ROLE_USER"]
  },
  "timestamp": "2025-04-19T12:34:56"
}
```

### Credit an account

```bash
curl -X POST http://localhost:8080/account/credit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "accountId": "account-id-here",
    "amount": 100.00
  }'
```

### Debit an account

```bash
curl -X POST http://localhost:8080/account/debit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "accountId": "account-id-here",
    "amount": 50.00
  }'
```

## 📂 Project Structure

```
online-bank/
├── pom.xml (Parent POM)
├── common/ (Shared resources)
├── service-discovery/ (Eureka Server)
├── config-server/ (Configuration Server)
├── api-gateway/ (API Gateway)
├── auth-service/ (User Authentication)
└── accounting-service/ (Account Management)
```

## ✨ Features

- 👤 User registration and authentication with JWT
- 💰 Account management (credit and debit operations)
- 📝 Transaction history tracking
- 🔒 Secure API with Spring Security
- ⚙️ Centralized configuration
- 🔍 Service discovery
- 🌐 API gateway routing
- 🗄️ MongoDB replica set for high availability
- 🐳 Dockerized deployment

### 🎁 Bonus Features

- 📚 OpenAPI documentation with Springdoc
- 🧪 Test containers for integration testing
- 🔍 Health checks and metrics with Spring Boot Actuator
- 🐳 Multi-stage Docker builds for optimized images
