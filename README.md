# SPMS — Day 10: Parking Service (Core)

## Goal
Stand up the core parking space model and inventory management.

## What's new in this snapshot
```
parking-service/
├── pom.xml
└── src/main/
    ├── java/com/spms/parkingservice/
    │   ├── ParkingServiceApplication.java
    │   ├── entity/ParkingSpace.java, ParkingStatus.java
    │   ├── repository/ParkingSpaceRepository.java
    │   ├── dto/ParkingSpaceRequest.java, ParkingSpaceResponse.java
    │   ├── service/ParkingSpaceService.java
    │   ├── controller/ParkingSpaceController.java
    │   └── exception/GlobalExceptionHandler.java, ResourceNotFoundException.java, ApiError.java
    └── resources/application.yml
```
Everything from Days 1–9 (Eureka, Config Server, Gateway, User Service,
Vehicle Service) is carried forward unchanged.

## Model
`ParkingSpace`: `id`, `location`, `zone`, `price`, `status`
(`AVAILABLE` / `RESERVED` / `OCCUPIED`), `ownerId`, plus reservation
tracking fields (`reservedByUserId`, `reservedVehicleId`, `reservedAt`) that
Day 11 will start using.

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/spaces` | Create a new parking space |
| GET | `/spaces` | List all parking spaces |
| GET | `/spaces/{id}` | Get a single parking space |

### Sample request — create a space
```http
POST http://localhost:8083/spaces
Content-Type: application/json

{
  "location": "Colombo City Center",
  "zone": "Zone A",
  "price": 100.00,
  "ownerId": 2
}
```
New spaces default to `status: "AVAILABLE"`.

Also reachable through the Gateway: `http://localhost:8080/api/spaces`.

## How to run
```bash
mvn clean install

# terminal 1-5: eureka-server, config-server, api-gateway, user-service, vehicle-service
# terminal 6
cd parking-service && mvn spring-boot:run
```

## Verify
- Eureka dashboard (**http://localhost:8761**) → `PARKING-SERVICE` now listed.
- **http://localhost:8083/actuator/health** → `UP`.
- H2 console (dev only): **http://localhost:8083/h2-console**, JDBC URL `jdbc:h2:mem:parkingdb`.
