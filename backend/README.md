# Atrang 2026–27 Spring Boot REST API

Backend application for Atrang 2026–27 festival event management and administrative operations at IIIT Tiruchirappalli.

## Technologies
- **Java 21 (LTS)**
- **Spring Boot 3.3.4**
- **Spring Security** (Session-based auth with HttpOnly cookies & BCrypt)
- **Spring Data JPA** (Hibernate 6)
- **PostgreSQL 18** (Production & Local development)
- **Flyway** (Database migrations)
- **Maven**

## Build & Run Instructions

### Prerequisites
- JDK 21
- Maven 3.9+
- PostgreSQL 16+ running on `localhost:5432` with database `atrang`

### Environment Variables
| Variable | Default Value | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/atrang` | PostgreSQL connection JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | `atrang_app` | PostgreSQL user |
| `SPRING_DATASOURCE_PASSWORD` | `atrang_secret_pass` | PostgreSQL user password |
| `ATRANG_ADMIN_USERNAME` | `admin` | Initial admin username |
| `ATRANG_ADMIN_PASSWORD` | `admin123` | Initial admin password (hashed on startup) |
| `ATRANG_FRONTEND_ORIGIN` | `http://localhost:3000` | Allowed CORS frontend URL |

### Running Locally
```bash
# Set JAVA_HOME to JDK 21
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
export PATH=$JAVA_HOME/bin:$PATH

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.
