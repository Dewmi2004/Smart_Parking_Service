package com.spms.parkingservice.specification;

import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Composable filter predicates for GET /spaces. Each method returns null
 * when its filter isn't supplied, so Specification.where(...).and(...)
 * cleanly skips absent filters instead of needing a chain of if-statements.
 */
public final class ParkingSpaceSpecifications {

    private ParkingSpaceSpecifications() {
    }

    public static Specification<ParkingSpace> hasLocation(String location) {
        if (location == null || location.isBlank()) {
            return null;
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<ParkingSpace> hasZone(String zone) {
        if (zone == null || zone.isBlank()) {
            return null;
        }
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("zone")), "%" + zone.toLowerCase() + "%");
    }

    public static Specification<ParkingSpace> hasStatus(ParkingStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<ParkingSpace> priceGreaterThanOrEqual(BigDecimal minPrice) {
        if (minPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<ParkingSpace> priceLessThanOrEqual(BigDecimal maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
