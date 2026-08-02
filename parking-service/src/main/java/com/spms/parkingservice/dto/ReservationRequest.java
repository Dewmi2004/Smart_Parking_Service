package com.spms.parkingservice.dto;

import jakarta.validation.constraints.NotNull;

public class ReservationRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    private Long vehicleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
