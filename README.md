# SPMS — Day 7: User Enhancements (Booking History)

## Goal
Add booking history storage to the User Service so a user's past/active
parking bookings can be logged and retrieved.

## What's new in this snapshot
```
user-service/src/main/java/com/spms/userservice/
├── entity/Booking.java, BookingStatus.java           (new)
├── repository/BookingRepository.java                  (new)
├── dto/BookingRequest.java, BookingResponse.java       (new)
├── service/BookingService.java                         (new)
└── controller/BookingController.java                   (new)
```
Everything from Day 6 (user registration/list/update) is unchanged.

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/users/register` | Register a new user or owner |
| GET | `/users` | List all users |
| GET | `/users/{id}` | Get a single user |
| PUT | `/users/{id}` | Update name / phone / password |
| **POST** | **`/users/{userId}/bookings`** | **Add a booking history record** |
| **GET** | **`/users/{userId}/bookings`** | **Get a user's booking history** |

### Sample request — add a booking record
```http
POST http://localhost:8081/users/1/bookings
Content-Type: application/json

{
  "vehicleId": 10,
  "parkingSpaceId": 5,
  "location": "Colombo City Center, Zone A",
  "startTime": "2026-07-29T09:00:00",
  "endTime": "2026-07-29T11:30:00",
  "amount": 350.00,
  "status": "COMPLETED"
}
```
Returns `201 Created`. If `userId` doesn't exist → `404 Not Found`.

### Sample request — get booking history
```http
GET http://localhost:8081/users/1/bookings
```
Returns a list of `BookingResponse` objects, most recent first.

> Note: since the Parking, Vehicle, and Payment services don't exist yet,
> booking records are added directly through this API for now. Once those
> services are built (Days 10–15), they'll call this endpoint automatically
> whenever a real reservation/payment completes.

Also reachable through the Gateway: `http://localhost:8080/api/users/1/bookings`.

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
1. Register a user via `POST /users/register`, note the returned `id`.
2. Add a booking via `POST /users/{id}/bookings`.
3. Confirm it shows up via `GET /users/{id}/bookings`.
4. Check the H2 console (**http://localhost:8081/h2-console**, JDBC URL
   `jdbc:h2:mem:userdb`) — you should see both `USERS` and `BOOKINGS` tables.
