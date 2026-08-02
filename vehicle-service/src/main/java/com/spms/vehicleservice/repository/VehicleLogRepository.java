package com.spms.vehicleservice.repository;

import com.spms.vehicleservice.entity.LogStatus;
import com.spms.vehicleservice.entity.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {

    List<VehicleLog> findByVehicleIdOrderByEntryTimeDesc(Long vehicleId);

    Optional<VehicleLog> findByVehicleIdAndStatus(Long vehicleId, LogStatus status);
}
