package com.spms.parkingservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds the "parking.reservation" section of application.yml (or the
 * Config Server's parking-service.yml) so reservation expiry timing can be
 * tuned without a code change or redeploy.
 */
@Component
@ConfigurationProperties(prefix = "parking.reservation")
public class ParkingReservationProperties {

    /** How long a RESERVED space stays held before it's auto-released. */
    private long expiryMinutes = 15;

    /** How often the background sweep checks for expired reservations. */
    private long expiryCheckIntervalMs = 60000;

    public long getExpiryMinutes() {
        return expiryMinutes;
    }

    public void setExpiryMinutes(long expiryMinutes) {
        this.expiryMinutes = expiryMinutes;
    }

    public long getExpiryCheckIntervalMs() {
        return expiryCheckIntervalMs;
    }

    public void setExpiryCheckIntervalMs(long expiryCheckIntervalMs) {
        this.expiryCheckIntervalMs = expiryCheckIntervalMs;
    }
}
