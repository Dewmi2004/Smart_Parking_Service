package com.spms.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * User Service - manages user & parking-owner accounts for SPMS.
 *
 * Business logic owned by this service:
 *  - Register / authenticate users
 *  - View and update user profiles
 *  - Access booking history and logs (Day 7)
 *
 * Registers with Eureka as "USER-SERVICE" and pulls its config
 * (port, datasource) from the Config Server.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
