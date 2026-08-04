package com.spms.paymentservice.dto;

import com.spms.paymentservice.entity.Payment;
import com.spms.paymentservice.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long userId;
    private Long vehicleId;
    private Long parkingSpaceId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String transactionRef;
    private String maskedCardNumber;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUserId();
        this.vehicleId = payment.getVehicleId();
        this.parkingSpaceId = payment.getParkingSpaceId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.transactionRef = payment.getTransactionRef();
        this.maskedCardNumber = payment.getMaskedCardNumber();
        this.failureReason = payment.getFailureReason();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
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

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
