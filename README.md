# Smart Parking Management System (SPMS)

A cloud-native, microservice-based platform for real-time parking search,
reservation, vehicle tracking, and payment — built with Spring Boot and
Spring Cloud.

> **Status: Phase 1 complete** — Service Registry, Config Server, and API
> Gateway are implemented and wired together. Business microservices
> (User, Vehicle, Parking, Payment, Analytics) are the next phase.

## Architecture

```
                        ┌─────────────────────┐
                        │   API Gateway (8080) │  <-- single entry point
                        └──────────┬───────────┘
                                   │ routes via Eureka
        ┌───────────┬─────────────┼─────────────┬────────────┐
        ▼            ▼             ▼             ▼            ▼
   User Service  Vehicle Svc  Parking Svc   Payment Svc  Analytics Svc
     (8081)        (8082)       (8083)        (8084)        (8085)
        │            │             │             │            │
        └────────────┴─────┬───────┴─────────────┴────────────┘
                            ▼
                   Eureka Server (8761)   Config Server (8888)
                 (service discovery)    (centralized config)
```

- **Eureka Server** — every microservice registers here so others can find
  it dynamically instead of using hardcoded hosts/ports.
- **Config Server** — serves shared and per-service configuration from
  `config-server/config-repo/` so settings can change without redeploying.
- **API Gateway** — the single entry point clients call; it routes
  `/api/users/**`, `/api/vehicles/**`, `/api/spaces/**`, `/api/payments/**`
  to the right backend service via Eureka-based load balancing (`lb://`).

## Tech Stack

| Technology | Purpose |
|---|---|
| Spring Boot 3.2.5 | Core framework for each microservice |
| Spring Cloud Eureka | Service registry & discovery |
| Spring Cloud Config | Centralized configuration |
| Spring Cloud Gateway | API Gateway / single entry point |
| Java 17, Maven | Language & build tool |
| Postman | API testing |

## Project Structure

```
smart-parking-system/
├── pom.xml                    # parent/aggregator POM
├── eureka-server/             # Service registry (port 8761)
├── config-server/             # Centralized config (port 8888)
│   └── config-repo/           # config files served to microservices
├── api-gateway/                # Single entry point (port 8080)
├── docs/screenshots/           # Eureka dashboard screenshot goes here
└── postman_collection.json     # exported Postman collection
```

*(Business microservices — `user-service`, `vehicle-service`,
`parking-service`, `payment-service` — will be added as sibling modules in
the next phase, each registering with Eureka and pulling config from the
Config Server.)*

## How to Run (Phase 1)

Build everything from the root:

```bash
mvn clean install
```

Start services **in this order** (each in its own terminal):

```bash
# 1. Service registry — must start first
cd eureka-server && mvn spring-boot:run

# 2. Config server — depends on Eureka being reachable
cd config-server && mvn spring-boot:run

# 3. API Gateway — depends on Eureka
cd api-gateway && mvn spring-boot:run
```

Then verify:

- Eureka dashboard: http://localhost:8761
- Config Server health: http://localhost:8888/actuator/health
- Config Server sample lookup: http://localhost:8888/parking-service/default
- API Gateway health: http://localhost:8080/actuator/health

Once business microservices are added, they'll show up on the Eureka
dashboard automatically as they start.

## Resources

- [Postman Collection](./postman_collection.json)
- ![Eureka Dashboard](./docs/screenshots/eureka_dashboard.png)

## Roadmap

- [x] Day 1 — Environment setup
- [x] Day 2 — Core microservices skeleton (Eureka, Config, Gateway)
- [x] Day 3 — Eureka Server
- [x] Day 4 — Config Server
- [x] Day 5 — API Gateway routing
- [ ] Day 6–7 — User Service + booking history
- [ ] Day 8–9 — Vehicle Service + entry/exit logs
- [ ] Day 10–11 — Parking Service + search filters
- [ ] Day 12–13 — Dynamic pricing + reservation expiry
- [ ] Day 14–15 — Payment Service + receipts
- [ ] Day 16 — Analytics Service
- [ ] Day 17 — Notification simulation
- [ ] Day 18 — Integration testing
- [ ] Day 19 — Postman collection + documentation
- [ ] Day 20 — Final testing & submission
