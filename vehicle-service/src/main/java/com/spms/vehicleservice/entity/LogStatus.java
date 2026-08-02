package com.spms.vehicleservice.entity;

public enum LogStatus {
    /** Vehicle has entered but not yet exited. */
    ACTIVE,
    /** Vehicle has entered and exited; duration is finalized. */
    COMPLETED
}
