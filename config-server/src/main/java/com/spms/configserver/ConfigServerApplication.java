package com.spms.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server - Centralized configuration management for SPMS.
 * Serves configuration files (stored in the local `config-repo` folder,
 * or a Git repo in production) to every microservice so settings can be
 * changed without redeploying services.
 *
 * Runs on port 8888. Registers itself with Eureka so it can be discovered
 * as "config-server" by other services if using discovery-first lookup.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
