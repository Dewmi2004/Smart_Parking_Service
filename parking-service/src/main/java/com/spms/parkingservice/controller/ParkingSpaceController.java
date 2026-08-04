package com.spms.parkingservice.controller;

import com.spms.parkingservice.dto.ParkingSpaceRequest;
import com.spms.parkingservice.dto.ParkingSpaceResponse;
import com.spms.parkingservice.dto.ParkingSpaceUpdateRequest;
import com.spms.parkingservice.dto.ReservationRequest;
import com.spms.parkingservice.entity.ParkingStatus;
import com.spms.parkingservice.scheduler.ReservationExpiryScheduler;
import com.spms.parkingservice.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;
    private final ReservationExpiryScheduler reservationExpiryScheduler;

    public ParkingSpaceController(ParkingSpaceService parkingSpaceService,
                                   ReservationExpiryScheduler reservationExpiryScheduler) {
        this.parkingSpaceService = parkingSpaceService;
        this.reservationExpiryScheduler = reservationExpiryScheduler;
    }

    // POST /spaces — create a new parking space
    @PostMapping
    public ResponseEntity<ParkingSpaceResponse> createSpace(@Valid @RequestBody ParkingSpaceRequest request) {
        ParkingSpaceResponse response = parkingSpaceService.createSpace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /spaces — list/search parking spaces, with optional filters:
    // ?location=&zone=&minPrice=&maxPrice=&status=AVAILABLE
    // Any combination of filters can be supplied; omitted ones are ignored.
    @GetMapping
    public ResponseEntity<List<ParkingSpaceResponse>> getSpaces(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) ParkingStatus status) {

        boolean noFiltersGiven = location == null && zone == null
                && minPrice == null && maxPrice == null && status == null;

        List<ParkingSpaceResponse> results = noFiltersGiven
                ? parkingSpaceService.getAllSpaces()
                : parkingSpaceService.searchSpaces(location, zone, minPrice, maxPrice, status);

        return ResponseEntity.ok(results);
    }

    // GET /spaces/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpaceResponse> getSpaceById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSpaceService.getSpaceById(id));
    }

    // PUT /spaces/{id} — update location/zone/price
    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpaceResponse> updateSpace(@PathVariable Long id,
                                                              @Valid @RequestBody ParkingSpaceUpdateRequest request) {
        return ResponseEntity.ok(parkingSpaceService.updateSpace(id, request));
    }

    // PUT /spaces/{id}/reserve — reserve an AVAILABLE space
    @PutMapping("/{id}/reserve")
    public ResponseEntity<ParkingSpaceResponse> reserveSpace(@PathVariable Long id,
                                                               @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(parkingSpaceService.reserveSpace(id, request));
    }

    // PUT /spaces/{id}/release — release a RESERVED/OCCUPIED space back to AVAILABLE
    @PutMapping("/{id}/release")
    public ResponseEntity<ParkingSpaceResponse> releaseSpace(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSpaceService.releaseSpace(id));
    }

    // POST /spaces/expire-check — manually trigger the reservation-expiry sweep
    // immediately, instead of waiting for the background scheduler's next tick
    // or for parking.reservation.expiry-minutes to actually elapse. Handy for
    // testing/demoing Day 13 without sitting around for real minutes to pass.
    @PostMapping("/expire-check")
    public ResponseEntity<Map<String, Integer>> triggerExpiryCheck() {
        int released = reservationExpiryScheduler.releaseExpiredReservationsNow();
        return ResponseEntity.ok(Map.of("releasedCount", released));
    }
}
