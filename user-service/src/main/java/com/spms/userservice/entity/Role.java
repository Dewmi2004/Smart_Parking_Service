package com.spms.userservice.entity;

/**
 * Distinguishes drivers (end users) from parking space owners,
 * per the two user-facing stakeholder roles in the SPMS spec.
 */
public enum Role {
    USER,
    OWNER
}
