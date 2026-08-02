package com.spms.parkingservice.dto;

import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSpaceResponse {

    private Long id;
    private String location;
    private String zone;
    private BigDecimal price;
    private ParkingStatus status;
    private Long ownerId;
    private Long reservedByUserId;
    private Long reservedVehicleId;
    private LocalDateTime reservedAt;
    private LocalDateTime createdAt;

    public ParkingSpaceResponse(ParkingSpace space) {
        this.id = space.getId();
        this.location = space.getLocation();
        this.zone = space.getZone();
        this.price = space.getPrice();
        this.status = space.getStatus();
        this.ownerId = space.getOwnerId();
        this.reservedByUserId = space.getReservedByUserId();
        this.reservedVehicleId = space.getReservedVehicleId();
        this.reservedAt = space.getReservedAt();
        this.createdAt = space.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getZone() {
        return zone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ParkingStatus getStatus() {
        return status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getReservedByUserId() {
        return reservedByUserId;
    }

    public Long getReservedVehicleId() {
        return reservedVehicleId;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
