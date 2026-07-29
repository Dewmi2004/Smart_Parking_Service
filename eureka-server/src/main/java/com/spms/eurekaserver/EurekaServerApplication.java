package com.spms.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server - Service Registry & Discovery for the Smart Parking
 * Management System (SPMS). All microservices (User, Vehicle, Parking,
 * Payment, Analytics, etc.) register themselves here so they can discover
 * and call one another without hardcoded hostnames/ports.
 *
 * Dashboard available at: http://localhost:8761
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
