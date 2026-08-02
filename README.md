# Smart Parking Management System (SPMS)

A cloud-native, microservice-based platform for real-time parking search,
reservation, vehicle tracking, and payment — built with Spring Boot and
Spring Cloud.

> **Status: Day 9 complete.** Infrastructure (Eureka, Config Server,
> Gateway), User Service (with booking history), and Vehicle Service (with
> entry/exit tracking) are done. Parking, Payment, and Analytics services
> are the next phase.

## Architecture

```
                        ┌─────────────────────┐
                        │   API Gateway (8080) │  <-- single entry point
                        └──────────┬───────────┘
                                   │ routes via Eureka
        ┌───────────┬─────────────┼─────────────┬────────────┐
        ▼            ▼             ▼             ▼            ▼
   User Service  Vehicle Svc  Parking Svc*  Payment Svc*  Analytics Svc*
     (8081)        (8082)       (8083)        (8084)        (8085)
        │            │
        └─────┬──────┘   (Vehicle Service calls User Service directly
              │            over Eureka to validate vehicle ownership)
              ▼
     Eureka Server (8761)   Config Server (8888)
   (service discovery)    (centralized config)

   * not yet built — planned for Days 10-16
```

## Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3.2.5 | Core framework for each microservice |
| Spring Cloud Eureka | Service registry & discovery |
| Spring Cloud Config | Centralized configuration |
| Spring Cloud Gateway | API Gateway / single entry point |
| Spring Data JPA + H2 | Persistence for User & Vehicle Service |
| Java 17, Maven | Language & build tool |
| Postman | API testing |

## Project Structure

```
smart-parking-system/
├── pom.xml                    # parent/aggregator POM (5 modules)
├── eureka-server/             # Service registry (port 8761)
├── config-server/             # Centralized config (port 8888)
│   └── config-repo/           # config files served to microservices
├── api-gateway/                # Single entry point (port 8080)
├── user-service/               # Users + booking history (port 8081)
├── vehicle-service/            # Vehicles + entry/exit logs (port 8082)
├── docs/screenshots/            # Eureka dashboard screenshot goes here
└── postman_collection.json      # exported Postman collection
```

## How to Run

Build everything from the root:
```bash
mvn clean install
```

Start services **in this order**, each in its own terminal:
```bash
cd eureka-server   && mvn spring-boot:run   # 1. must start first
cd config-server   && mvn spring-boot:run   # 2. depends on Eureka
cd api-gateway     && mvn spring-boot:run   # 3. depends on Eureka
cd user-service    && mvn spring-boot:run   # 4. depends on Eureka/Config
cd vehicle-service && mvn spring-boot:run   # 5. calls User Service on vehicle registration
```

Verify:
- Eureka dashboard: http://localhost:8761 — all five services should be listed
- API Gateway health: http://localhost:8080/actuator/health

## API Reference

### User Service (`localhost:8081`, or via Gateway `localhost:8080/api/users`)

| Method | Path | Description |
|---|---|---|
| POST | `/users/register` | Register a new user or owner |
| GET | `/users` | List all users |
| GET | `/users/{id}` | Get a single user |
| PUT | `/users/{id}` | Update name / phone / password |
| POST | `/users/{userId}/bookings` | Add a booking history record |
| GET | `/users/{userId}/bookings` | Get a user's booking history |

### Vehicle Service (`localhost:8082`, or via Gateway `localhost:8080/api/vehicles`)

| Method | Path | Description |
|---|---|---|
| POST | `/vehicles` | Register a vehicle, linked to a `userId` |
| GET | `/vehicles` | List all vehicles |
| GET | `/vehicles/{id}` | Get a single vehicle |
| GET | `/vehicles/user/{userId}` | All vehicles owned by a user |
| PUT | `/vehicles/{id}` | Update model / color / type |
| **POST** | **`/vehicles/{vehicleId}/entry`** | **Simulate the vehicle entering (starts a session)** |
| **POST** | **`/vehicles/{vehicleId}/exit`** | **Simulate the vehicle exiting (computes stay duration)** |
| **GET** | **`/vehicles/{vehicleId}/logs`** | **Full entry/exit history for a vehicle** |

### Sample flow — register, enter, exit
```http
### 1. Register a user
POST http://localhost:8081/users/register
Content-Type: application/json

{ "name": "Nadeesha Perera", "email": "nadeesha@example.com", "password": "secret123", "role": "USER" }

### 2. Register a vehicle for that user (use the returned id)
POST http://localhost:8082/vehicles
Content-Type: application/json

{ "plateNumber": "WP CAB-1234", "model": "Toyota Aqua", "color": "White", "type": "CAR", "userId": 1 }

### 3. Vehicle enters the parking area
POST http://localhost:8082/vehicles/1/entry
Content-Type: application/json

{ "parkingSpaceId": 5 }

### 4. Vehicle exits — duration is calculated automatically
POST http://localhost:8082/vehicles/1/exit

### 5. View the full log history
GET http://localhost:8082/vehicles/1/logs
```

A vehicle can only have **one active (not-yet-exited) session at a time** —
trying to record a second entry before exiting returns `409 Conflict`, and
trying to exit with no active session also returns `409 Conflict`.

## Resources

- [Postman Collection](./postman_collection.json)
- ![Eureka Dashboard](./docs/screenshots/eureka_dashboard.png)

## Roadmap

- [x] Day 1 — Environment setup
- [x] Day 2 — Core microservices skeleton
- [x] Day 3 — Eureka Server
- [x] Day 4 — Config Server
- [x] Day 5 — API Gateway routing
- [x] Day 6 — User Service (core)
- [x] Day 7 — User Service enhancements (booking history)
- [x] Day 8 — Vehicle Service (core, linked to users)
- [x] Day 9 — Vehicle entry/exit logs with duration calculation
- [ ] Day 10–11 — Parking Service + search filters
- [ ] Day 12–13 — Dynamic pricing + reservation expiry
- [ ] Day 14–15 — Payment Service + receipts
- [ ] Day 16 — Analytics Service
- [ ] Day 17 — Notification simulation
- [ ] Day 18 — Integration testing
- [ ] Day 19 — Postman collection + documentation
- [ ] Day 20 — Final testing & submission
