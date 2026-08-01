# SPMS — Day 6: User Service (Core)

## Goal
Stand up user management: registration, listing, and profile updates.

## What's new in this snapshot
```
user-service/
├── pom.xml
└── src/main/
    ├── java/com/spms/userservice/
    │   ├── UserServiceApplication.java
    │   ├── entity/User.java, Role.java
    │   ├── repository/UserRepository.java
    │   ├── dto/UserRegistrationRequest.java, UserUpdateRequest.java, UserResponse.java
    │   ├── service/UserService.java
    │   ├── controller/UserController.java
    │   └── exception/GlobalExceptionHandler.java, ResourceNotFoundException.java,
    │       DuplicateEmailException.java, ApiError.java
    └── resources/application.yml
```
(Eureka Server, Config Server, and API Gateway are carried forward unchanged from Days 3–5.)

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/users/register` | Register a new user or owner |
| GET | `/users` | List all users |
| GET | `/users/{id}` | Get a single user |
| PUT | `/users/{id}` | Update name / phone / password |

### Sample request — register
```http
POST http://localhost:8081/users/register
Content-Type: application/json

{
  "name": "Nadeesha Perera",
  "email": "nadeesha@example.com",
  "password": "secret123",
  "phone": "+94771234567",
  "role": "USER"
}
```
Returns `201 Created` with the user (password never included in responses).

Duplicate email → `409 Conflict`. Invalid fields (blank name, bad email, short
password) → `400 Bad Request` with a `fieldErrors` map.

Also reachable through the Gateway once it's running:
`http://localhost:8080/api/users/register` (Gateway rewrites `/api/users/**` → `/users/**`).

## How to run
```bash
mvn clean install

# terminal 1
cd eureka-server && mvn spring-boot:run
# terminal 2
cd config-server && mvn spring-boot:run
# terminal 3
cd api-gateway && mvn spring-boot:run
# terminal 4
cd user-service && mvn spring-boot:run
```

## Verify
- Eureka dashboard (**http://localhost:8761**) → `USER-SERVICE` now listed.
- **http://localhost:8081/actuator/health** → `UP`.
- H2 console (dev only): **http://localhost:8081/h2-console** — JDBC URL `jdbc:h2:mem:userdb`, user `sa`, blank password.

## Config notes
- Port: `8081`
- Uses `spring.config.import: optional:configserver:...` — pulls config from
  the Config Server if it's up, otherwise falls back to this file's own
  settings, so `user-service` can still be run standalone for testing.
- Passwords are stored as plain text for assignment scope — a real system
  would hash them (e.g. BCrypt) before persisting.
