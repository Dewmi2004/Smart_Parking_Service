package com.spms.vehicleservice.client;

import com.spms.vehicleservice.exception.ExternalServiceException;
import com.spms.vehicleservice.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Talks to User Service to confirm a userId is real before linking a
 * vehicle to it. Uses the Eureka application name ("user-service") in the
 * URL rather than a hardcoded host:port, resolved at call time by the
 * load-balanced RestTemplate.
 */
@Component
public class UserServiceClient {

    private static final String USER_SERVICE_URL = "http://user-service/users/{id}";

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Throws ResourceNotFoundException if the user doesn't exist, or
     * ExternalServiceException if User Service can't be reached at all.
     */
    public void verifyUserExists(Long userId) {
        try {
            restTemplate.getForEntity(USER_SERVICE_URL, String.class, userId);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        } catch (ResourceAccessException | HttpClientErrorException ex) {
            throw new ExternalServiceException(
                    "Could not verify user " + userId + " — User Service is unreachable or returned an error");
        }
    }
}
