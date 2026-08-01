# SPMS — Day 4: Config Server

## Goal
Add centralized configuration management so every microservice can fetch its
settings from one place instead of hardcoding them.

## What's in this snapshot
```
smart-parking-system/
├── pom.xml                    # parent POM (eureka-server + config-server)
├── eureka-server/              # from Day 3
└── config-server/
    ├── pom.xml
    ├── src/main/
    │   ├── java/com/spms/configserver/ConfigServerApplication.java
    │   └── resources/application.yml
    └── config-repo/            # config files served to microservices
        ├── application.yml     # shared config (Eureka URL, actuator, logging)
        ├── user-service.yml    # pre-staged for Day 6
        ├── vehicle-service.yml # pre-staged for Day 8
        ├── parking-service.yml # pre-staged for Day 10, incl. pricing rules
        └── payment-service.yml # pre-staged for Day 14
```

## How to run
```bash
mvn clean install

# terminal 1
cd eureka-server && mvn spring-boot:run

# terminal 2 (wait a few seconds after Eureka is up)
cd config-server && mvn spring-boot:run
```

## Verify
- Eureka dashboard (**http://localhost:8761**) — `CONFIG-SERVER` should now
  appear under registered instances.
- **http://localhost:8888/actuator/health** → `{"status":"UP"}`
- **http://localhost:8888/parking-service/default** → returns the merged
  `application.yml` + `parking-service.yml` config as JSON, proving the
  server is serving files correctly.

## Config notes
- Port: `8888`
- Profile: `native` — reads config files straight from the local
  `config-repo/` folder (no Git repo needed for this assignment).
- Registers with Eureka like any other client service.
