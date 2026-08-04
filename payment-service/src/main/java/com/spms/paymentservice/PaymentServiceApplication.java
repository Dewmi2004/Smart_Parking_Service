package com.spms.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Payment Service - handles internal (mock) payment transactions for
 * parking fees.
 *
 * Business logic owned by this service:
 *  - Validate mock card/payment data
 *  - Simulate transaction flow and status (PENDING -> SUCCESS/FAILED)
 *  - Generate digital receipts (Day 15)
 *
 * Registers with Eureka as "PAYMENT-SERVICE" and pulls its config
 * (port, datasource) from the Config Server.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
