# SPMS — Day 13: Reservation Expiry

## Goal
Stop no-shows from holding a parking space forever: automatically release
a reservation if the driver hasn't arrived within a configured time window.

## What's new in this snapshot
```
parking-service/src/main/java/com/spms/parkingservice/
├── config/ParkingReservationProperties.java   (new — binds parking.reservation.*)
├── scheduler/ReservationExpiryScheduler.java   (new — @Scheduled sweep + manual trigger)
├── repository/ParkingSpaceRepository.java       (updated — findByStatusAndReservedAtBefore)
├── controller/ParkingSpaceController.java        (updated — POST /spaces/expire-check)
└── ParkingServiceApplication.java                  (updated — @EnableScheduling)
```
Everything from Days 1–12 (Eureka, Config Server, Gateway, User Service,
Vehicle Service, Parking Service with filters/reserve/release/dynamic
pricing) is carried forward unchanged.

## How it works
A background job (`ReservationExpiryScheduler`) runs every
`expiry-check-interval-ms` (default **60 seconds**) and looks for any space
still `RESERVED` whose `reservedAt` timestamp is older than
`expiry-minutes` (default **15 minutes**). Each match is automatically
flipped back to `AVAILABLE` and its reservation fields cleared — exactly
like calling `PUT /spaces/{id}/release` yourself, just automatic.

Configurable in `application.yml` (or via Config Server):
```yaml
parking:
  reservation:
    expiry-minutes: 15
    expiry-check-interval-ms: 60000
```

## New endpoint (for testing/demoing)

| Method | Path | Description |
|---|---|---|
| **POST** | **`/spaces/expire-check`** | **Manually run the expiry sweep right now** |

Waiting a real 15 minutes to see auto-release happen isn't practical during
grading/testing, so this endpoint runs the exact same sweep logic
on-demand. Returns e.g. `{"releasedCount": 2}`.

### Sample flow — see it in action
```http
### 1. Reserve a space
PUT http://localhost:8083/spaces/1/reserve
Content-Type: application/json

{ "userId": 5, "vehicleId": 10 }

### 2. Temporarily set expiry-minutes to 0 in application.yml and restart
###    (or just wait — the real scheduler will catch it after 15 min anyway)

### 3. Force an immediate sweep instead of waiting
POST http://localhost:8083/spaces/expire-check

### 4. Confirm it's released
GET http://localhost:8083/spaces/1
```
With `expiry-minutes: 0`, any active reservation is immediately "expired"
the moment you hit `/expire-check`, which is the fastest way to verify the
logic without editing timestamps directly in the H2 console.

## How to run
```bash
mvn clean install
# start eureka-server, config-server, api-gateway, user-service,
# vehicle-service, then:
cd parking-service && mvn spring-boot:run
```
Watch the parking-service console log — successful sweeps that actually
released something log a line like:
```
Reservation expiry sweep: auto-released 1 expired reservation(s)
```

## Verify
1. Reserve a space.
2. Check its `status` is `RESERVED` and `reservedAt` is populated (`GET /spaces/{id}`).
3. Set `parking.reservation.expiry-minutes: 0` in `application.yml`, restart the service.
4. Call `POST /spaces/expire-check` — response shows `releasedCount: 1` (or however many were reserved).
5. `GET /spaces/{id}` again — `status` is back to `AVAILABLE`, reservation fields are `null`.
6. Set `expiry-minutes` back to a real value (e.g. `15`) once you're done testing.
