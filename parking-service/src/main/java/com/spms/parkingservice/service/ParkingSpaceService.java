package com.spms.parkingservice.service;

import com.spms.parkingservice.dto.*;
import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;
import com.spms.parkingservice.exception.InvalidStateException;
import com.spms.parkingservice.exception.ResourceNotFoundException;
import com.spms.parkingservice.repository.ParkingSpaceRepository;
import com.spms.parkingservice.specification.ParkingSpaceSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final PricingService pricingService;

    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository, PricingService pricingService) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.pricingService = pricingService;
    }

    public ParkingSpaceResponse createSpace(ParkingSpaceRequest request) {
        ParkingSpace space = new ParkingSpace(
                request.getLocation(),
                request.getZone(),
                request.getPrice(),
                request.getOwnerId()
        );
        ParkingSpace saved = parkingSpaceRepository.save(space);
        return toResponseWithPricing(saved);
    }

    public List<ParkingSpaceResponse> getAllSpaces() {
        return parkingSpaceRepository.findAll()
                .stream()
                .map(this::toResponseWithPricing)
                .toList();
    }

    /**
     * Smart search: any combination of location, zone, price range, and
     * availability status. Absent filters are simply skipped (see
     * ParkingSpaceSpecifications — each returns null when unset).
     */
    public List<ParkingSpaceResponse> searchSpaces(String location, String zone,
                                                     BigDecimal minPrice, BigDecimal maxPrice,
                                                     ParkingStatus status) {
        Specification<ParkingSpace> spec = Specification
                .where(ParkingSpaceSpecifications.hasLocation(location))
                .and(ParkingSpaceSpecifications.hasZone(zone))
                .and(ParkingSpaceSpecifications.priceGreaterThanOrEqual(minPrice))
                .and(ParkingSpaceSpecifications.priceLessThanOrEqual(maxPrice))
                .and(ParkingSpaceSpecifications.hasStatus(status));

        return parkingSpaceRepository.findAll(spec)
                .stream()
                .map(this::toResponseWithPricing)
                .toList();
    }

    public ParkingSpaceResponse getSpaceById(Long id) {
        return toResponseWithPricing(findSpaceOrThrow(id));
    }

    public ParkingSpaceResponse updateSpace(Long id, ParkingSpaceUpdateRequest request) {
        ParkingSpace space = findSpaceOrThrow(id);

        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            space.setLocation(request.getLocation());
        }
        if (request.getZone() != null && !request.getZone().isBlank()) {
            space.setZone(request.getZone());
        }
        if (request.getPrice() != null) {
            space.setPrice(request.getPrice());
        }

        return toResponseWithPricing(parkingSpaceRepository.save(space));
    }

    /** Reserve an AVAILABLE space for a user/vehicle. */
    public ParkingSpaceResponse reserveSpace(Long id, ReservationRequest request) {
        ParkingSpace space = findSpaceOrThrow(id);

        if (space.getStatus() != ParkingStatus.AVAILABLE) {
            throw new InvalidStateException(
                    "Parking space " + id + " is not available (current status: " + space.getStatus() + ")");
        }

        space.setStatus(ParkingStatus.RESERVED);
        space.setReservedByUserId(request.getUserId());
        space.setReservedVehicleId(request.getVehicleId());
        space.setReservedAt(LocalDateTime.now());

        return toResponseWithPricing(parkingSpaceRepository.save(space));
    }

    /** Release a RESERVED or OCCUPIED space back to AVAILABLE. */
    public ParkingSpaceResponse releaseSpace(Long id) {
        ParkingSpace space = findSpaceOrThrow(id);

        if (space.getStatus() == ParkingStatus.AVAILABLE) {
            throw new InvalidStateException("Parking space " + id + " is already available");
        }

        space.setStatus(ParkingStatus.AVAILABLE);
        space.setReservedByUserId(null);
        space.setReservedVehicleId(null);
        space.setReservedAt(null);

        return toResponseWithPricing(parkingSpaceRepository.save(space));
    }

    protected ParkingSpace findSpaceOrThrow(Long id) {
        return parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking space not found with id: " + id));
    }

    /** Attaches the live effective price + zone occupancy rate to a response. */
    private ParkingSpaceResponse toResponseWithPricing(ParkingSpace space) {
        BigDecimal effectivePrice = pricingService.calculateEffectivePrice(space);
        double occupancyRate = pricingService.getZoneOccupancyRate(space.getZone());
        return new ParkingSpaceResponse(space, effectivePrice, occupancyRate);
    }
}
