# SPMS — Day 3: Eureka Server

## Goal
Stand up the service registry so future microservices have somewhere to register.

## What's in this snapshot
```
smart-parking-system/
├── pom.xml               # parent POM (only eureka-server module for now)
└── eureka-server/
    ├── pom.xml
    └── src/main/
        ├── java/com/spms/eurekaserver/EurekaServerApplication.java
        └── resources/application.yml
```

## How to run
```bash
mvn clean install
cd eureka-server
mvn spring-boot:run
```

## Verify
Open **http://localhost:8761** — you should see the Eureka dashboard.
No instances will be registered yet (that starts on Day 4 once Config Server joins).

## Config notes
- Port: `8761`
- `register-with-eureka: false` / `fetch-registry: false` — the registry itself
  doesn't register with itself.
- Self-preservation mode is disabled for local dev so dead instances drop off quickly.
