package com.parkway.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"parking_lot_id", "slot_number"})
})
public class ParkingSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    @JsonProperty("slot_id")
    private Long slotId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private Admin admin;
    
    @Column(name = "slot_number", nullable = false)
    @JsonProperty("slot_number")
    private Integer slotNumber;
    
    @Column(name = "status", length = 20)
    @JsonProperty("status")
    private String status = "vacant";
    
    @Column(name = "reserved")
    @JsonProperty("reserved")
    private Boolean reserved = false;
    
    @Column(name = "booking_id")
    @JsonProperty("booking_id")
    @JsonAlias({"bookingId"})
    private Long bookingId;
    
    @Column(name = "user_id")
    @JsonProperty("user_id")
    @JsonAlias({"userId"})
    private Long userId;
    
    @Column(name = "vehicle_id")
    @JsonProperty("vehicle_id")
    @JsonAlias({"vehicleId"})
    private Long vehicleId;
    
    @Column(name = "user_firstname", length = 100)
    @JsonProperty("user_firstname")
    @JsonAlias({"userFirstname", "user_firstname"})
    private String userFirstname;
    
    @Column(name = "user_lastname", length = 100)
    @JsonProperty("user_lastname")
    @JsonAlias({"userLastname", "user_lastname"})
    private String userLastname;
    
    @Column(name = "vehicle_type", length = 50)
    @JsonProperty("vehicle_type")
    @JsonAlias({"vehicleType"})
    private String vehicleType;
    
    @Column(name = "vehicle_model", length = 100)
    @JsonProperty("vehicle_model")
    @JsonAlias({"vehicleModel", "model"})
    private String vehicleModel;
    
    @Column(name = "plate_number", length = 50)
    @JsonProperty("plate_number")
    @JsonAlias({"plateNumber"})
    private String plateNumber;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public ParkingSlot() {
    }
    
    // Constructor
    public ParkingSlot(Admin admin, Integer slotNumber) {
        this.admin = admin;
        this.slotNumber = slotNumber;
        this.status = "vacant";
    }
    
    // Getters and Setters
    public Long getSlotId() {
        return slotId;
    }
    
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }
    
    public Admin getAdmin() {
        return admin;
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public Integer getSlotNumber() {
        return slotNumber;
    }
    
    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Boolean getReserved() {
        return reserved;
    }
    
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }
    
    public Long getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getVehicleModel() {
        return vehicleModel;
    }
    
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    
    public String getPlateNumber() {
        return plateNumber;
    }
    
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    
    @JsonProperty("parking_lot_id")
    public Long getParkingLotId() {
        return admin != null ? admin.getStaffID() : null;
    }
}
