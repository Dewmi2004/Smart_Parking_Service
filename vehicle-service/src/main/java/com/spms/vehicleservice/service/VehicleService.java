package com.spms.vehicleservice.service;

import com.spms.vehicleservice.client.UserServiceClient;
import com.spms.vehicleservice.dto.VehicleRegistrationRequest;
import com.spms.vehicleservice.dto.VehicleResponse;
import com.spms.vehicleservice.dto.VehicleUpdateRequest;
import com.spms.vehicleservice.entity.Vehicle;
import com.spms.vehicleservice.exception.DuplicatePlateException;
import com.spms.vehicleservice.exception.ResourceNotFoundException;
import com.spms.vehicleservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserServiceClient userServiceClient;

    public VehicleService(VehicleRepository vehicleRepository, UserServiceClient userServiceClient) {
        this.vehicleRepository = vehicleRepository;
        this.userServiceClient = userServiceClient;
    }

    public VehicleResponse registerVehicle(VehicleRegistrationRequest request) {
        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new DuplicatePlateException(
                    "A vehicle with plate number '" + request.getPlateNumber() + "' already exists");
        }

        // Link vehicle to user — confirm the user actually exists in User Service first.
        userServiceClient.verifyUserExists(request.getUserId());

        Vehicle vehicle = new Vehicle(
                request.getPlateNumber(),
                request.getModel(),
                request.getColor(),
                request.getType(),
                request.getUserId()
        );

        Vehicle saved = vehicleRepository.save(vehicle);
        return new VehicleResponse(saved);
    }

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponse::new)
                .toList();
    }

    public VehicleResponse getVehicleById(Long id) {
        return new VehicleResponse(findVehicleOrThrow(id));
    }

    public List<VehicleResponse> getVehiclesByUser(Long userId) {
        // Confirm the user exists so callers get a clean 404 instead of a
        // silently empty list for a typo'd/nonexistent userId.
        userServiceClient.verifyUserExists(userId);

        return vehicleRepository.findByUserId(userId)
                .stream()
                .map(VehicleResponse::new)
                .toList();
    }

    public VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = findVehicleOrThrow(id);

        if (request.getModel() != null && !request.getModel().isBlank()) {
            vehicle.setModel(request.getModel());
        }
        if (request.getColor() != null && !request.getColor().isBlank()) {
            vehicle.setColor(request.getColor());
        }
        if (request.getType() != null) {
            vehicle.setType(request.getType());
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        return new VehicleResponse(saved);
    }

    private Vehicle findVehicleOrThrow(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }
}
