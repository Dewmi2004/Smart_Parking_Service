package com.spms.parkingservice.service;

import com.spms.parkingservice.dto.ParkingSpaceRequest;
import com.spms.parkingservice.dto.ParkingSpaceResponse;
import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.exception.ResourceNotFoundException;
import com.spms.parkingservice.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public ParkingSpaceResponse createSpace(ParkingSpaceRequest request) {
        ParkingSpace space = new ParkingSpace(
                request.getLocation(),
                request.getZone(),
                request.getPrice(),
                request.getOwnerId()
        );
        ParkingSpace saved = parkingSpaceRepository.save(space);
        return new ParkingSpaceResponse(saved);
    }

    public List<ParkingSpaceResponse> getAllSpaces() {
        return parkingSpaceRepository.findAll()
                .stream()
                .map(ParkingSpaceResponse::new)
                .toList();
    }

    public ParkingSpaceResponse getSpaceById(Long id) {
        return new ParkingSpaceResponse(findSpaceOrThrow(id));
    }

    protected ParkingSpace findSpaceOrThrow(Long id) {
        return parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking space not found with id: " + id));
    }
}
