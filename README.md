# SPMS — Day 11: Parking APIs + Filters

## Goal
Add smart search filters (location, zone, price range, availability) and
reserve/release actions on top of Day 10's core Parking Service.

## What's new in this snapshot
```
parking-service/src/main/java/com/spms/parkingservice/
├── specification/ParkingSpaceSpecifications.java   (new — dynamic filter predicates)
├── dto/ReservationRequest.java                       (new)
├── dto/ParkingSpaceUpdateRequest.java                 (new)
├── exception/InvalidStateException.java                (new)
├── repository/ParkingSpaceRepository.java              (updated — + JpaSpecificationExecutor)
├── service/ParkingSpaceService.java                    (updated — search/reserve/release/update)
└── controller/ParkingSpaceController.java               (updated — filters + reserve/release/update)
```
Everything from Days 1–10 (Eureka, Config Server, Gateway, User Service,
Vehicle Service, core Parking Service) is carried forward unchanged.

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/spaces` | Create a new parking space |
| **GET** | **`/spaces`** | **List/search — supports filters below** |
| GET | `/spaces/{id}` | Get a single parking space |
| **PUT** | **`/spaces/{id}`** | **Update location / zone / price** |
| **PUT** | **`/spaces/{id}/reserve`** | **Reserve an AVAILABLE space for a user/vehicle** |
| **PUT** | **`/spaces/{id}/release`** | **Release a RESERVED/OCCUPIED space back to AVAILABLE** |

### Search filters (all optional, any combination)
```
GET /spaces?location=colombo
GET /spaces?zone=Zone%20A
GET /spaces?minPrice=50&maxPrice=150
GET /spaces?status=AVAILABLE
GET /spaces?location=colombo&status=AVAILABLE&maxPrice=120
```
`location` and `zone` match case-insensitively as substrings. `status` is
one of `AVAILABLE`, `RESERVED`, `OCCUPIED`. Omitting all filters just
returns every space (same as Day 10's plain `GET /spaces`).

### Sample flow — reserve then release
```http
### Reserve space 1 for user 5 with vehicle 10
PUT http://localhost:8083/spaces/1/reserve
Content-Type: application/json

{ "userId": 5, "vehicleId": 10 }

### Release it again
PUT http://localhost:8083/spaces/1/release
```
Reserving a space that isn't `AVAILABLE`, or releasing one that's already
`AVAILABLE`, returns `409 Conflict`.

Also reachable through the Gateway: `http://localhost:8080/api/spaces?...`.

## How to run
```bash
mvn clean install
# start eureka-server, config-server, api-gateway, user-service,
# vehicle-service, then:
cd parking-service && mvn spring-boot:run
```

## Verify
1. Create a couple of spaces with different locations/zones/prices via `POST /spaces`.
2. Try filtered searches (`GET /spaces?zone=...&status=AVAILABLE`) and confirm only matching spaces come back.
3. Reserve one (`PUT /spaces/{id}/reserve`) → its `status` becomes `RESERVED` and `reservedByUserId`/`reservedVehicleId` populate.
4. Try reserving it again → `409 Conflict`.
5. Release it (`PUT /spaces/{id}/release`) → back to `AVAILABLE`, reservation fields cleared.
