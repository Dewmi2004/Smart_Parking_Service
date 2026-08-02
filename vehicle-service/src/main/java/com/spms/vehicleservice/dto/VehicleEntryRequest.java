package com.spms.vehicleservice.dto;

public class VehicleEntryRequest {

    /** Optional — which parking space it entered, if already known/reserved. */
    private Long parkingSpaceId;

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }
}
