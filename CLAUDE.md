# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Emart is a Spring Boot-based e-commerce platform API built with Java 17. It follows a modular architecture with distinct business domains organized under the `modules` package.

**Tech Stack:**
- Spring Boot 2.7.18 with Spring Security and Spring Data Redis
- MyBatis-Plus 3.5.3.1 for database operations
- MySQL with Druid connection pool
- JWT (jjwt 0.9.1) for authentication
- SpringDoc OpenAPI 1.7.0 for API documentation
- Hutool 5.8.11 utility library

## Build & Run Commands

### Build the project
```bash
./mvnw clean package
```

### Run the application
```bash
./mvnw spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/emart-api-1.0.0.jar
```

### Run tests
```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=EmartApplicationTests
```

### Package without tests
```bash
./mvnw clean package -DskipTests
```

## Architecture & Code Organization

### Package Structure

The codebase follows a modular architecture organized by business domain:

```
com.emart
├── common/              # Shared components
│   ├── api/            # CommonResult (unified API response wrapper), ResultCode
│   ├── config/         # Common configurations
│   ├── domain/         # Shared domain objects
│   ├── exception/      # Custom exception classes
│   └── service/        # Common service interfaces
├── config/             # Spring configurations (Security, SpringDoc)
├── controller/         # Test controllers
├── modules/            # Business modules (organized by domain)
│   ├── ums/            # User Management System (用户管理)
│   ├── pms/            # Product Management System (商品管理)
│   ├── oms/            # Order Management System (订单管理)
│   └── cms/            # Content Management System (内容管理)
├── security/           # Security & authentication
│   ├── component/      # JWT filters, security components
│   ├── config/         # Security configuration
│   └── util/           # JwtTokenUtil
└── EmartApplication    # Main application entry point
```

### Module Structure Pattern

Each module (ums/pms/oms/cms) follows this structure:
- **controller/** - REST API endpoints
- **dto/** - Data Transfer Objects for request/response
- **model/** - Database entities
- **mapper/** - MyBatis-Plus mappers for database operations
- **service/** - Service interfaces
- **service/impl/** - Service implementations

### Key Architectural Patterns

1. **Unified API Response**: All controllers return `CommonResult<T>` which wraps responses with standard format (code, message, data)

2. **JWT Authentication**: Stateless authentication using JWT tokens stored in Authorization header (`Bearer <token>`). JwtTokenUtility handles token generation/validation. Token expires after 7 days (configurable via `jwt.expiration`).

3. **Service Layer Pattern**: Services extend `ServiceImpl<Mapper, Model>` from MyBatis-Plus for basic CRUD operations. Business logic is implemented in service implementations.

4. **Password Encoding**: Uses BCryptPasswordEncoder for secure password hashing.

5. **DTO Pattern**: Controllers accept DTOs for requests (e.g., UserLoginParam, UserRegisterParam) which are validated using `@Valid`.

6. **MyBatis-Plus Integration**:
   - ID auto-generation: `id-type: auto`
   - Camel case conversion enabled: `map-underscore-to-camel-case: true`
   - Mapper locations: `classpath:/mapper/**/*.xml`

## Configuration

### Profiles
- Default active profile: `dev`
- Configuration files: `application.yml` and `application-dev.yml`

### Database
- MySQL database: `emart`
- Default connection: `jdbc:mysql://localhost:3306/emart`
- Druid connection pool with min 5, max 20 connections

### Redis
- Used for caching (cart, browse history)
- Default: `localhost:6379`, database 0

### Security Configuration
Current security setup in `SecurityConfig`:
- CSRF disabled
- Permits access to `/test/**`, Swagger UI, API docs
- Currently permits all requests (`.anyRequest().permitAll()`) - planned to enforce authentication

### API Documentation
- Swagger UI available at `/swagger-ui/**`
- OpenAPI docs at `/v3/api-docs/**`
- Accessible without authentication

## Development Notes

### User Module (Current Implementation)
- Registration: Creates user with BCrypt-encoded password, status=1
- Login: Validates credentials, returns JWT token
- Token includes: userId, username (sub), creation time
- Password is always cleared before returning user data

### TODOs & Future Work
- JWT authentication filter implementation (currently UserController.getUserInfo has TODO for JWT parsing)
- Enforce authentication for protected endpoints (currently `.anyRequest().permitAll()`)
- Implement remaining modules (pms, oms, cms)

### Database Entity Conventions
- Base fields typically include: id, createTime, status
- Password field is set to null when returning user data to frontend
- Timestamps stored as `Date` type

### API Response Standards
Use `CommonResult` methods:
- `CommonResult.success(data)` - Success with data
- `CommonResult.success(data, message)` - Success with custom message
- `CommonResult.failed(message)` - Failure with message
- `CommonResult.validateFailed(message)` - Validation errors
- `CommonResult.unauthorized(data)` - Not logged in (401)
- `CommonResult.forbidden(data)` - No permission (403)

### File Upload
- Configured upload path: `D:/emart/uploads/`
- Max file size: 10MB

## Port & Access
- Default port: `8080`
- Application URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
