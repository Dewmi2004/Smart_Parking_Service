package com.spms.vehicleservice.dto;

import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.entity.VehicleType;

import java.time.LocalDateTime;

public class VehicleResponse {

    private Long id;
    private String plateNumber;
    private String model;
    private String color;
    private VehicleType type;
    private Long userId;
    private LocalDateTime createdAt;

    public VehicleResponse(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.plateNumber = vehicle.getPlateNumber();
        this.model = vehicle.getModel();
        this.color = vehicle.getColor();
        this.type = vehicle.getType();
        this.userId = vehicle.getUserId();
        this.createdAt = vehicle.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public VehicleType getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
