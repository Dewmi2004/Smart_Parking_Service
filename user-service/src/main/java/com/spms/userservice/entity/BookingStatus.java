package com.spms.userservice.entity;

/**
 * Lifecycle status of a booking record shown in a user's history.
 * Statuses are set by whichever service records the booking (today: manually
 * via the API for testing; later: the Parking/Payment services once built).
 */
public enum BookingStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
