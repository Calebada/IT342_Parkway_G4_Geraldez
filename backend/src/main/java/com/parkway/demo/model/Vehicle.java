package com.parkway.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "plate_number", nullable = false, unique = true, length = 50)
    private String plateNumber;
    
    @Column(name = "model", nullable = false, length = 100)
    private String model;
    
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private String vehicleType;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // Default constructor
    public Vehicle() {
    }
    
    // Constructor with all fields
    public Vehicle(User user, String plateNumber, String model, String vehicleType) {
        this.user = user;
        this.plateNumber = plateNumber;
        this.model = model;
        this.vehicleType = vehicleType;
    }
    
    // Getters and Setters
    public Long getVehicleID() {
        return vehicleID;
    }
    
    public void setVehicleID(Long vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID=" + vehicleID +
                ", userId=" + (user != null ? user.getUserID() : null) +
                ", plateNumber='" + plateNumber + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
