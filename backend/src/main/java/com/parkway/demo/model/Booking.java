package com.parkway.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    @JsonProperty("booking_id")
    private Long bookingId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    private Admin admin;
    
    @Column(name = "vehicle_id")
    @JsonProperty("vehicle_id")
    private Long vehicleId;
    
    @Column(name = "slot_id")
    @JsonProperty("slot_id")
    private Long slotId;
    
    @Column(name = "date_reserved", nullable = false)
    @JsonProperty("date_reserved")
    private LocalDate dateReserved;
    
    @Column(name = "time_in", nullable = false)
    @JsonProperty("time_in")
    private LocalTime timeIn;
    
    @Column(name = "time_out", nullable = false)
    @JsonProperty("time_out")
    private LocalTime timeOut;
    
    @Column(name = "vehicle_type", nullable = false, length = 50)
    @JsonProperty("vehicle_type")
    private String vehicleType;
    
    @Column(name = "duration", nullable = false)
    @JsonProperty("duration")
    private Integer duration;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    
    @Column(name = "status", length = 20)
    @JsonProperty("status")
    private String status = "pending";
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public Booking() {
    }
    
    // Constructor with fields
    public Booking(User user, Admin admin, LocalDate dateReserved, LocalTime timeIn, 
                   LocalTime timeOut, String vehicleType, Integer duration, 
                   BigDecimal totalPrice) {
        this.user = user;
        this.admin = admin;
        this.dateReserved = dateReserved;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.vehicleType = vehicleType;
        this.duration = duration;
        this.totalPrice = totalPrice;
        this.status = "pending";
    }
    
    // Getters and Setters
    public Long getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Admin getAdmin() {
        return admin;
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public Long getSlotId() {
        return slotId;
    }
    
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }
    
    public LocalDate getDateReserved() {
        return dateReserved;
    }
    
    public void setDateReserved(LocalDate dateReserved) {
        this.dateReserved = dateReserved;
    }
    
    public LocalTime getTimeIn() {
        return timeIn;
    }
    
    public void setTimeIn(LocalTime timeIn) {
        this.timeIn = timeIn;
    }
    
    public LocalTime getTimeOut() {
        return timeOut;
    }
    
    public void setTimeOut(LocalTime timeOut) {
        this.timeOut = timeOut;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + (user != null ? user.getUserID() : null) +
                ", parkingLotId=" + (admin != null ? admin.getStaffID() : null) +
                ", dateReserved=" + dateReserved +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                ", vehicleType='" + vehicleType + '\'' +
                ", duration=" + duration +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
