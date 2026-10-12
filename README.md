# Java Digital Banking Backend

This repository contains a Java Spring Boot backend for a digital banking application.

## Features
- User registration and login
- JWT-based security
- Account creation and balance tracking
- Internal funds transfer
- Transaction history
- MySQL data persistence

## Tech Stack
- Java 17
- Spring Boot 3.3.x
- Spring Security
- Spring Data JPA
- MySQL
- Maven

## Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8+

## Database Setup
Create a MySQL database named `digital_banking_db`.

```sql
CREATE DATABASE digital_banking_db;
```

Update the database credentials in `src/main/resources/application.properties` if needed.

## Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

The app will run on:
- http://localhost:8080

## Key API Endpoints

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Accounts
- `GET /api/accounts`
- `POST /api/accounts`
- `GET /api/accounts/{id}`
- `GET /api/accounts/{id}/transactions`

### Transactions
- `POST /api/transactions/transfer`
- `GET /api/transactions`

### User
- `GET /api/users/me`

## Example Login Payload
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

## Notes
The application uses JWT authentication. Include the token in the Authorization header as:

```http
Authorization: Bearer <jwt_token>
```
