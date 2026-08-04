package com.spms.paymentservice.entity;

public enum PaymentStatus {
    /** Transaction has been created but not yet processed. */
    PENDING,
    /** Mock gateway accepted the transaction. */
    SUCCESS,
    /** Mock gateway declined the transaction (invalid/expired card, etc.). */
    FAILED
}
