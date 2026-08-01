package com.spms.vehicleservice.controller;

import com.spms.vehicleservice.dto.VehicleRegistrationRequest;
import com.spms.vehicleservice.dto.VehicleResponse;
import com.spms.vehicleservice.dto.VehicleUpdateRequest;
import com.spms.vehicleservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // POST /vehicles — register a vehicle and link it to a user
    @PostMapping
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRegistrationRequest request) {
        VehicleResponse response = vehicleService.registerVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /vehicles — list all vehicles
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // GET /vehicles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // GET /vehicles/user/{userId} — all vehicles belonging to one user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VehicleResponse>> getVehiclesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByUser(userId));
    }

    // PUT /vehicles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Long id,
                                                           @Valid @RequestBody VehicleUpdateRequest request) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, request));
    }
}
