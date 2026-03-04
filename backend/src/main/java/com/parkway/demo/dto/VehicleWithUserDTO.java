package com.parkway.demo.dto;

import java.time.LocalDateTime;

public class VehicleWithUserDTO {
    private Long vehicleId;
    private Long userId;
    private String plateNumber;
    private String model;
    private String vehicleType;
    private LocalDateTime createdAt;
    private String userFirstname;
    private String userLastname;
    
    // Default constructor
    public VehicleWithUserDTO() {
    }
    
    // Constructor with all fields
    public VehicleWithUserDTO(Long vehicleId, Long userId, String plateNumber, String model, 
                              String vehicleType, LocalDateTime createdAt, 
                              String userFirstname, String userLastname) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.vehicleType = vehicleType;
        this.createdAt = createdAt;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
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
    
    public String getUserFirstname() {
        return userFirstname;
    }
    
    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }
    
    public String getUserLastname() {
        return userLastname;
    }
    
    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }
}
