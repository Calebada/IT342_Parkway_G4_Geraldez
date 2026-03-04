package com.parkway.demo.dto;

import java.time.LocalDateTime;

public class VehicleDTO {
    private Long vehicleId;
    private Long userId;
    private String plateNumber;
    private String model;
    private String vehicleType;
    private LocalDateTime createdAt;
    
    // Default constructor
    public VehicleDTO() {
    }
    
    // Constructor with all fields
    public VehicleDTO(Long vehicleId, Long userId, String plateNumber, String model, 
                      String vehicleType, LocalDateTime createdAt) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.vehicleType = vehicleType;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
