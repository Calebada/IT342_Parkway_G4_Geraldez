package com.parkway.demo.dto;

public class VehicleRequest {
    private Long userId;
    private String plateNumber;
    private String model;
    private String vehicleType;
    
    // Default constructor
    public VehicleRequest() {
    }
    
    // Constructor with all fields
    public VehicleRequest(Long userId, String plateNumber, String model, String vehicleType) {
        this.userId = userId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.vehicleType = vehicleType;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getPlateNumber() {
        return plateNumber;
    }
    
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
