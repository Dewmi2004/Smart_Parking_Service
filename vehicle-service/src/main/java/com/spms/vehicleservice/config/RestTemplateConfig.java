package com.spms.vehicleservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * @LoadBalanced lets us call other services by their Eureka application
     * name (e.g. "http://user-service/users/5") instead of hardcoding a
     * host:port — Spring Cloud resolves it via the service registry.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
