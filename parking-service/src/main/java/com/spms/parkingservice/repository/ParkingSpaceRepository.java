package com.spms.parkingservice.repository;

import com.spms.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long>,
        JpaSpecificationExecutor<ParkingSpace> {
}
