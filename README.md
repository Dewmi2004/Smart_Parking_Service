# SPMS — Day 8: Vehicle Service (Core)

## Goal
Stand up vehicle management: registration, listing, and linking a vehicle
to its owning user (validated live against User Service).

## What's new in this snapshot
```
vehicle-service/
├── pom.xml
└── src/main/
    ├── java/com/spms/vehicleservice/
    │   ├── VehicleServiceApplication.java
    │   ├── entity/Vehicle.java, VehicleType.java
    │   ├── repository/VehicleRepository.java
    │   ├── dto/VehicleRegistrationRequest.java, VehicleUpdateRequest.java, VehicleResponse.java
    │   ├── client/UserServiceClient.java       ← calls User Service over Eureka
    │   ├── config/RestTemplateConfig.java       ← @LoadBalanced RestTemplate bean
    │   ├── service/VehicleService.java
    │   ├── controller/VehicleController.java
    │   └── exception/GlobalExceptionHandler.java, ResourceNotFoundException.java,
    │       DuplicatePlateException.java, ExternalServiceException.java, ApiError.java
    └── resources/application.yml
```
Eureka Server, Config Server, API Gateway, and User Service (through Day 7,
with booking history) are all carried forward unchanged.

## How "link vehicle to user" works
Rather than just storing a `userId` blindly, Vehicle Service calls
**User Service directly** over HTTP using a Eureka-resolved, load-balanced
`RestTemplate` (`http://user-service/users/{id}`) to confirm the user
actually exists before saving the vehicle. This is real inter-service
communication — not a shared database — which is the point of the
microservice architecture:
- User not found → `404 Not Found`
- User Service unreachable → `502 Bad Gateway` (`ExternalServiceException`)

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/vehicles` | Register a vehicle, linked to a `userId` |
| GET | `/vehicles` | List all vehicles |
| GET | `/vehicles/{id}` | Get a single vehicle |
| GET | `/vehicles/user/{userId}` | All vehicles owned by a user |
| PUT | `/vehicles/{id}` | Update model / color / type |

### Sample request — register a vehicle
```http
POST http://localhost:8082/vehicles
Content-Type: application/json

{
  "plateNumber": "WP CAB-1234",
  "model": "Toyota Aqua",
  "color": "White",
  "type": "CAR",
  "userId": 1
}
```
`userId` must belong to a real, already-registered user (see Day 6/7 —
`POST /users/register`), otherwise this returns `404 Not Found`.

Also reachable through the Gateway: `http://localhost:8080/api/vehicles`.

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
# terminal 5
cd vehicle-service && mvn spring-boot:run
```
Start **User Service before Vehicle Service** — registering a vehicle calls
out to User Service immediately, so it needs to already be up and
registered with Eureka.

## Verify
1. Register a user (`POST /users/register`), note the `id`.
2. Register a vehicle with that `userId` (`POST /vehicles`) → `201 Created`.
3. Try registering a vehicle with a made-up `userId` (e.g. `9999`) → `404 Not Found`.
4. `GET /vehicles/user/1` → returns that user's vehicle(s).
5. Eureka dashboard (**http://localhost:8761**) → `VEHICLE-SERVICE` now listed.
6. H2 console (dev only): **http://localhost:8082/h2-console**, JDBC URL `jdbc:h2:mem:vehicledb`.
