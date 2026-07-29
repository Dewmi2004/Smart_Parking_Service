package com.spms.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway - single entry point for all client requests into the SPMS
 * ecosystem. Routes requests to the correct backend microservice
 * (User, Vehicle, Parking, Payment, Analytics, ...) using Eureka service
 * discovery, so routes always resolve to live instances instead of
 * hardcoded hosts/ports.
 *
 * Runs on port 8080. Route definitions live in application.yml.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
