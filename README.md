# SPMS — Day 14: Payment Service

## Goal
Stand up mock payment processing for parking fees, with a realistic
PENDING → SUCCESS/FAILED transaction lifecycle.

## What's new in this snapshot
```
payment-service/
├── pom.xml
└── src/main/
    ├── java/com/spms/paymentservice/
    │   ├── PaymentServiceApplication.java
    │   ├── entity/Payment.java, PaymentStatus.java
    │   ├── repository/PaymentRepository.java
    │   ├── dto/PaymentRequest.java, PaymentResponse.java
    │   ├── service/PaymentService.java        ← mock card validation + status transitions
    │   ├── controller/PaymentController.java
    │   └── exception/GlobalExceptionHandler.java, ResourceNotFoundException.java, ApiError.java
    └── resources/application.yml
```
Everything from Days 1–13 (Eureka, Config Server, Gateway, User Service,
Vehicle Service, Parking Service) is carried forward unchanged.

## How the mock transaction works
`POST /payments` doesn't just save a row — it simulates a real gateway
call:
1. A `Payment` is saved with `status: PENDING` and a generated
   `transactionRef` (e.g. `TXN-4F2A9E1B7C3D5A80`).
2. The mock card is validated: 16-digit card number, 3–4 digit CVV, and an
   expiry date that hasn't passed.
3. The record is updated to `SUCCESS` (passes validation) or `FAILED`
   (with a `failureReason`), then returned.

**Card numbers are never stored in full** — only a masked version
(`**** **** **** 1234`) is persisted, same as any real system would do.

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/payments` | Process a mock payment |
| GET | `/payments` | List all payments |
| GET | `/payments/{id}` | Get a single payment |
| GET | `/payments/user/{userId}` | A user's payment history |

### Sample request — successful payment
```http
POST http://localhost:8084/payments
Content-Type: application/json

{
  "userId": 1,
  "vehicleId": 1,
  "parkingSpaceId": 3,
  "amount": 350.00,
  "cardNumber": "4111111111111111",
  "expiryMonth": 12,
  "expiryYear": 2027,
  "cvv": "123"
}
```
Returns `201 Created` with `status: "SUCCESS"`.

### Sample request — failed payment (expired card)
```http
POST http://localhost:8084/payments
Content-Type: application/json

{
  "userId": 1,
  "amount": 350.00,
  "cardNumber": "4111111111111111",
  "expiryMonth": 1,
  "expiryYear": 2020,
  "cvv": "123"
}
```
Returns `201 Created` with `status: "FAILED"` and
`failureReason: "Card has expired"`. (The HTTP status is still `201`
because the *request* to process a payment succeeded — the *transaction
outcome* is what failed, same as a real gateway response.)

Also reachable through the Gateway: `http://localhost:8080/api/payments`.

## How to run
```bash
mvn clean install
# start eureka-server, config-server, api-gateway, user-service,
# vehicle-service, parking-service, then:
cd payment-service && mvn spring-boot:run
```

## Verify
- Eureka dashboard (**http://localhost:8761**) → `PAYMENT-SERVICE` now listed.
- **http://localhost:8084/actuator/health** → `UP`.
- H2 console (dev only): **http://localhost:8084/h2-console**, JDBC URL `jdbc:h2:mem:paymentdb`.
- Try both a valid and an invalid card and confirm `status` differs accordingly.
