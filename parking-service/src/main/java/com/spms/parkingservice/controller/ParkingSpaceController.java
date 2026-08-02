package com.spms.parkingservice.controller;

import com.spms.parkingservice.dto.ParkingSpaceRequest;
import com.spms.parkingservice.dto.ParkingSpaceResponse;
import com.spms.parkingservice.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    // POST /spaces — create a new parking space
    @PostMapping
    public ResponseEntity<ParkingSpaceResponse> createSpace(@Valid @RequestBody ParkingSpaceRequest request) {
        ParkingSpaceResponse response = parkingSpaceService.createSpace(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /spaces — list all parking spaces
    @GetMapping
    public ResponseEntity<List<ParkingSpaceResponse>> getAllSpaces() {
        return ResponseEntity.ok(parkingSpaceService.getAllSpaces());
    }

    // GET /spaces/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpaceResponse> getSpaceById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSpaceService.getSpaceById(id));
    }
}
