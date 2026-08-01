package com.spms.vehicleservice.dto;

import com.spms.vehicleservice.entity.VehicleType;

public class VehicleUpdateRequest {

    private String model;

    private String color;

    private VehicleType type;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
