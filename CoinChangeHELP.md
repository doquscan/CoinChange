
# HELP.md

## Overview

This document provides a comprehensive guide to setting up, configuring, and running the Coin Change REST API application. The application provides a RESTful API for calculating optimal coin change for given bill amounts and logs each request for auditing purposes.

## Table of Contents

1. [Project Setup](#project-setup)
2. [Configuration](#configuration)
3. [Database Setup](#database-setup)
4. [Running the Application](#running-the-application)
5. [API Endpoints](#api-endpoints)
6. [Testing](#testing)
7. [Logging and Audit](#logging-and-audit)
8. [Troubleshooting](#troubleshooting)

## Project Setup

### Prerequisites

- Java 11 or higher
- Maven 3.6.3 or higher
- MySQL 5.7 or higher
- Liquibase (included in the Maven build)

### Cloning the Repository

```bash
git clone https://github.com/yourusername/coin-change-rest-api.git
cd coin-change-rest-api
```

## Configuration

### Application Properties

The application properties are configured in `src/main/resources/application.properties`. Key configurations include:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

# Valid bills and coin denominations
valid.bills=1,2,5,10,20,50,100
coin.denominations=0.25,0.10,0.05,0.01

# Security configurations
spring.security.user.name=your_username
spring.security.user.password=your_password
```

## Database Setup

The application uses MySQL as the database. Ensure that the MySQL service is running and a database is created for the application. Liquibase will handle the creation of tables and schema updates.

### Creating the Database

1. **Login to MySQL:**
   ```bash
   mysql -u root -p
   ```

2. **Create a database:**
   ```sql
   CREATE DATABASE your_database;
   ```

3. **Set up the database credentials in `application.properties`.**

## Running the Application

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Running the JAR File

After building the project, you can run the application using the generated JAR file:

```bash
java -jar target/coin-change-rest-api-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### 1. Get Change for a Bill

**Endpoint:** `/api/change`

**Method:** `POST`

**Parameters:**

- `bill` (integer): The bill amount for which change is requested.

**Response:**

- JSON object containing the coin denominations and their respective counts.

Example response for a $2 bill:

```json
{
    "0.25": 8,
    "0.10": 2,
    "0.05": 0,
    "0.01": 0
}
```

## Testing

### Running Unit Tests

To run unit tests, use the following Maven command:

```bash
mvn test
```

## Logging and Audit

Each request to the `/api/change` endpoint is logged for auditing purposes. The logs are stored in the `audit_log` table in the database, capturing details such as the bill amount, change provided, and timestamp.

### Example SQL for Audit Log Table

```sql
CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill INT NOT NULL,
    change VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
```

## Troubleshooting

### Common Issues

1. **Database Connection Issues:**
   - Ensure that the MySQL service is running and the credentials in `application.properties` are correct.

2. **Liquibase Migration Failures:**
   - Check the Liquibase changelog file for errors and ensure that the database user has the necessary permissions.

3. **Authentication Issues:**
   - Ensure that the credentials provided in `application.properties` for Spring Security match those used in the application.

## Contributions

Contributions to the project are welcome! Please fork the repository and create a pull request with your changes.
