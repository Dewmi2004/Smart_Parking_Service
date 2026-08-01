package com.spms.userservice.dto;

import com.spms.userservice.entity.Booking;
import com.spms.userservice.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long userId;
    private Long vehicleId;
    private Long parkingSpaceId;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal amount;
    private BookingStatus status;
    private LocalDateTime createdAt;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.userId = booking.getUser().getId();
        this.vehicleId = booking.getVehicleId();
        this.parkingSpaceId = booking.getParkingSpaceId();
        this.location = booking.getLocation();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
        this.amount = booking.getAmount();
        this.status = booking.getStatus();
        this.createdAt = booking.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
