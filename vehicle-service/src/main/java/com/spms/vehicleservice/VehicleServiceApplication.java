package com.spms.vehicleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Vehicle Service - manages vehicle registration for SPMS.
 *
 * Business logic owned by this service:
 *  - Register, update, and retrieve vehicle details
 *  - Link vehicles to users (validated via a call to User Service)
 *  - Simulate vehicle entry/exit tracking (Day 9)
 *
 * Registers with Eureka as "VEHICLE-SERVICE" and pulls its config
 * (port, datasource) from the Config Server.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class VehicleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceApplication.class, args);
    }
}
