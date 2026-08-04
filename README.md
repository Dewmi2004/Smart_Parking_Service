# SPMS — Day 12: Dynamic Pricing

## Goal
Make parking prices respond to real demand: raise price when a zone is
crowded, and optionally during configured peak hours.

## What's new in this snapshot
```
parking-service/src/main/java/com/spms/parkingservice/
├── config/ParkingPricingProperties.java     (new — binds parking.pricing.*)
├── service/PricingService.java               (new — occupancy + peak-hour calc)
├── repository/ParkingSpaceRepository.java     (updated — occupancy count queries)
├── dto/ParkingSpaceResponse.java               (updated — + effectivePrice, zoneOccupancyRate)
└── service/ParkingSpaceService.java             (updated — every response now includes live pricing)
```
Everything from Days 1–11 (Eureka, Config Server, Gateway, User Service,
Vehicle Service, Parking Service with filters/reserve/release) is carried
forward unchanged.

## How dynamic pricing works
Nothing is stored — `effectivePrice` is calculated **fresh on every read**
from two stacking multipliers:

1. **Occupancy surge**: if the space's zone (RESERVED + OCCUPIED spaces ÷
   total spaces in that zone) is above `high-occupancy-threshold` (default
   `0.8` = 80%), price × `high-occupancy-multiplier` (default `1.5`).
2. **Peak hour**: if `peak-hour-enabled` and the current time falls inside
   any configured window, price × `peak-hour-multiplier` (default `1.2`).

Both can apply at once (e.g. a crowded zone during evening rush = 1.5 × 1.2
= 1.8× the base price).

Configurable in `application.yml` (or via Config Server):
```yaml
parking:
  pricing:
    high-occupancy-threshold: 0.8
    high-occupancy-multiplier: 1.5
    peak-hour-enabled: true
    peak-hour-multiplier: 1.2
    peak-hours:
      - start: "08:00"
        end: "10:00"
      - start: "17:00"
        end: "19:00"
```

## Response shape
Every `GET /spaces`, `GET /spaces/{id}`, `POST /spaces`, `PUT /spaces/{id}`,
reserve, and release response now includes:
```json
{
  "id": 1,
  "location": "Colombo City Center",
  "zone": "Zone A",
  "price": 100.00,
  "effectivePrice": 150.00,
  "zoneOccupancyRate": 0.83,
  "status": "AVAILABLE",
  ...
}
```
- `price` — the space's stored base price (unchanged from Day 10/11).
- `effectivePrice` — what you'd actually pay right now.
- `zoneOccupancyRate` — 0.0–1.0, how full that zone currently is.

## How to run
```bash
mvn clean install
# start eureka-server, config-server, api-gateway, user-service,
# vehicle-service, then:
cd parking-service && mvn spring-boot:run
```

## Verify
1. Create several spaces in the same zone (e.g. 5 spaces, "Zone A").
2. Reserve 4 of them (`PUT /spaces/{id}/reserve`) — that zone is now 80% occupied.
3. `GET /spaces?zone=Zone%20A` — occupied-zone spaces should now show
   `effectivePrice` higher than `price` (surge multiplier applied).
4. To test peak-hour pricing without waiting for the actual time, temporarily
   edit the `peak-hours` window in `application.yml` to include the current
   time, restart, and re-check `effectivePrice`.
