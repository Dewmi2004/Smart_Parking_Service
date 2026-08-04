package com.spms.parkingservice.service;

import com.spms.parkingservice.config.ParkingPricingProperties;
import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;
import com.spms.parkingservice.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;

/**
 * Computes a parking space's live "effective price" from its base price:
 *  - Surge pricing: if the space's zone is above the configured occupancy
 *    threshold (default 80%), the price is multiplied up.
 *  - Peak-hour pricing: if the current time falls in a configured peak
 *    window (e.g. morning/evening rush), a second multiplier applies.
 * Both multipliers stack. Nothing is persisted — this is calculated fresh
 * on every read so it always reflects current occupancy and time of day.
 */
@Service
public class PricingService {

    private static final List<ParkingStatus> OCCUPIED_STATUSES = List.of(ParkingStatus.RESERVED, ParkingStatus.OCCUPIED);

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingPricingProperties pricingProperties;

    public PricingService(ParkingSpaceRepository parkingSpaceRepository, ParkingPricingProperties pricingProperties) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.pricingProperties = pricingProperties;
    }

    /** Occupancy ratio (0.0-1.0) for a zone: (reserved + occupied) / total. */
    public double getZoneOccupancyRate(String zone) {
        long total = parkingSpaceRepository.countByZone(zone);
        if (total == 0) {
            return 0.0;
        }
        long occupied = parkingSpaceRepository.countByZoneAndStatusIn(zone, OCCUPIED_STATUSES);
        return (double) occupied / total;
    }

    public boolean isPeakHourNow() {
        if (!pricingProperties.isPeakHourEnabled()) {
            return false;
        }
        LocalTime now = LocalTime.now();
        return pricingProperties.getPeakHours().stream().anyMatch(window -> window.contains(now));
    }

    /** The live price a customer would pay right now for this space. */
    public BigDecimal calculateEffectivePrice(ParkingSpace space) {
        double occupancyRate = getZoneOccupancyRate(space.getZone());
        double multiplier = 1.0;

        if (occupancyRate > pricingProperties.getHighOccupancyThreshold()) {
            multiplier *= pricingProperties.getHighOccupancyMultiplier();
        }
        if (isPeakHourNow()) {
            multiplier *= pricingProperties.getPeakHourMultiplier();
        }

        return space.getPrice()
                .multiply(BigDecimal.valueOf(multiplier))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
