package com.parkway.demo.dto;

public class VehicleResponse {
    private Long vehicleId;
    private String message;
    
    // Default constructor
    public VehicleResponse() {
    }
    
    // Constructor with all fields
    public VehicleResponse(Long vehicleId, String message) {
        this.vehicleId = vehicleId;
        this.message = message;
    }
    
    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
