package com.spms.paymentservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    private Long vehicleId;

    private Long parkingSpaceId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "cardNumber is required")
    private String cardNumber;

    @NotNull(message = "expiryMonth is required")
    @Min(value = 1, message = "expiryMonth must be between 1 and 12")
    @Max(value = 12, message = "expiryMonth must be between 1 and 12")
    private Integer expiryMonth;

    @NotNull(message = "expiryYear is required")
    private Integer expiryYear;

    @NotBlank(message = "cvv is required")
    private String cvv;

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

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
