package com.spms.parkingservice.dto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public class ParkingSpaceUpdateRequest {

    private String location;

    private String zone;

    @DecimalMin(value = "0.0", inclusive = true, message = "price must be zero or positive")
    private BigDecimal price;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
