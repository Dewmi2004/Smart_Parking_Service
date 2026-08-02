package com.spms.vehicleservice.controller;

import com.spms.vehicleservice.dto.VehicleEntryRequest;
import com.spms.vehicleservice.dto.VehicleLogResponse;
import com.spms.vehicleservice.service.VehicleLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles/{vehicleId}")
public class VehicleLogController {

    private final VehicleLogService vehicleLogService;

    public VehicleLogController(VehicleLogService vehicleLogService) {
        this.vehicleLogService = vehicleLogService;
    }

    // POST /vehicles/{vehicleId}/entry — simulate vehicle entering
    @PostMapping("/entry")
    public ResponseEntity<VehicleLogResponse> recordEntry(@PathVariable Long vehicleId,
                                                            @RequestBody(required = false) VehicleEntryRequest request) {
        VehicleLogResponse response = vehicleLogService.recordEntry(vehicleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /vehicles/{vehicleId}/exit — simulate vehicle exiting; computes duration
    @PostMapping("/exit")
    public ResponseEntity<VehicleLogResponse> recordExit(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleLogService.recordExit(vehicleId));
    }

    // GET /vehicles/{vehicleId}/logs — full entry/exit history for a vehicle
    @GetMapping("/logs")
    public ResponseEntity<List<VehicleLogResponse>> getLogs(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleLogService.getLogsForVehicle(vehicleId));
    }
}
