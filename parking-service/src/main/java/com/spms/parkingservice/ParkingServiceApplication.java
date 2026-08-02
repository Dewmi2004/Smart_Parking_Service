package com.spms.parkingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Parking Service - manages parking space inventory for SPMS.
 *
 * Business logic owned by this service:
 *  - List and manage parking spaces
 *  - Reserve and release parking spaces
 *  - Update status as occupied/available
 *  - Filter by location, zone, price range, availability (Day 11)
 *
 * Registers with Eureka as "PARKING-SERVICE" and pulls its config
 * (port, datasource, pricing rules) from the Config Server.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ParkingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }
}
