package com.spms.vehicleservice.service;

import com.spms.vehicleservice.dto.VehicleEntryRequest;
import com.spms.vehicleservice.dto.VehicleLogResponse;
import com.spms.vehicleservice.entity.LogStatus;
import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.entity.VehicleLog;
import com.spms.vehicleservice.exception.InvalidLogStateException;
import com.spms.vehicleservice.exception.ResourceNotFoundException;
import com.spms.vehicleservice.repository.VehicleLogRepository;
import com.spms.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleLogService {

    private final VehicleLogRepository vehicleLogRepository;
    private final VehicleRepository vehicleRepository;

    public VehicleLogService(VehicleLogRepository vehicleLogRepository, VehicleRepository vehicleRepository) {
        this.vehicleLogRepository = vehicleLogRepository;
        this.vehicleRepository = vehicleRepository;
    }

    /** Simulates a vehicle entering the parking area. */
    public VehicleLogResponse recordEntry(Long vehicleId, VehicleEntryRequest request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        vehicleLogRepository.findByVehicleIdAndStatus(vehicleId, LogStatus.ACTIVE)
                .ifPresent(existing -> {
                    throw new InvalidLogStateException(
                            "Vehicle " + vehicleId + " already has an active session (log id " + existing.getId()
                                    + ") — it must exit before entering again");
                });

        VehicleLog log = new VehicleLog();
        log.setVehicle(vehicle);
        log.setParkingSpaceId(request != null ? request.getParkingSpaceId() : null);
        log.setEntryTime(LocalDateTime.now());
        log.setStatus(LogStatus.ACTIVE);

        VehicleLog saved = vehicleLogRepository.save(log);
        return new VehicleLogResponse(saved);
    }

    /** Simulates a vehicle exiting the parking area and computes stay duration. */
    public VehicleLogResponse recordExit(Long vehicleId) {
        VehicleLog activeLog = vehicleLogRepository.findByVehicleIdAndStatus(vehicleId, LogStatus.ACTIVE)
                .orElseThrow(() -> new InvalidLogStateException(
                        "Vehicle " + vehicleId + " has no active session to exit from"));

        activeLog.recordExit();
        VehicleLog saved = vehicleLogRepository.save(activeLog);
        return new VehicleLogResponse(saved);
    }

    public List<VehicleLogResponse> getLogsForVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }

        return vehicleLogRepository.findByVehicleIdOrderByEntryTimeDesc(vehicleId)
                .stream()
                .map(VehicleLogResponse::new)
                .toList();
    }
}
