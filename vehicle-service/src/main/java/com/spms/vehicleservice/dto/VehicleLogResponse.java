package com.spms.vehicleservice.dto;

import com.spms.vehicleservice.entity.LogStatus;
import com.spms.vehicleservice.entity.VehicleLog;

import java.time.LocalDateTime;

public class VehicleLogResponse {

    private Long id;
    private Long vehicleId;
    private String plateNumber;
    private Long parkingSpaceId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long durationMinutes;
    private LogStatus status;
    private LocalDateTime createdAt;

    public VehicleLogResponse(VehicleLog log) {
        this.id = log.getId();
        this.vehicleId = log.getVehicle().getId();
        this.plateNumber = log.getVehicle().getPlateNumber();
        this.parkingSpaceId = log.getParkingSpaceId();
        this.entryTime = log.getEntryTime();
        this.exitTime = log.getExitTime();
        this.durationMinutes = log.getDurationMinutes();
        this.status = log.getStatus();
        this.createdAt = log.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public LogStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
