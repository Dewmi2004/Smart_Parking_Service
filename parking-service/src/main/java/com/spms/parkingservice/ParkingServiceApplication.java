package com.spms.parkingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Parking Service - manages parking space inventory for SPMS.
 *
 * Business logic owned by this service:
 *  - List and manage parking spaces
 *  - Reserve and release parking spaces
 *  - Update status as occupied/available
 *  - Filter by location, zone, price range, availability
 *  - Dynamic (surge + peak-hour) pricing
 *  - Auto-expire stale reservations (Day 13, via @Scheduled sweep)
 *
 * Registers with Eureka as "PARKING-SERVICE" and pulls its config
 * (port, datasource, pricing rules) from the Config Server.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class ParkingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }
}
