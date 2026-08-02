package com.spms.parkingservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingStatus status;

    /** The parking owner's user ID in User Service (role OWNER). Not a JPA
     *  relation since User lives in a separate microservice/database. */
    @Column(name = "owner_id")
    private Long ownerId;

    /** Set when a space is reserved/occupied — who and which vehicle. */
    @Column(name = "reserved_by_user_id")
    private Long reservedByUserId;

    @Column(name = "reserved_vehicle_id")
    private Long reservedVehicleId;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ParkingSpace() {
    }

    public ParkingSpace(String location, String zone, BigDecimal price, Long ownerId) {
        this.location = location;
        this.zone = zone;
        this.price = price;
        this.ownerId = ownerId;
        this.status = ParkingStatus.AVAILABLE;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ParkingStatus.AVAILABLE;
        }
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ParkingStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingStatus status) {
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getReservedByUserId() {
        return reservedByUserId;
    }

    public void setReservedByUserId(Long reservedByUserId) {
        this.reservedByUserId = reservedByUserId;
    }

    public Long getReservedVehicleId() {
        return reservedVehicleId;
    }

    public void setReservedVehicleId(Long reservedVehicleId) {
        this.reservedVehicleId = reservedVehicleId;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
