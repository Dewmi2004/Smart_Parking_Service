package com.spms.parkingservice.repository;

import com.spms.parkingservice.entity.ParkingSpace;
import com.spms.parkingservice.entity.ParkingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long>,
        JpaSpecificationExecutor<ParkingSpace> {

    long countByZone(String zone);

    long countByZoneAndStatusIn(String zone, Collection<ParkingStatus> statuses);
}
