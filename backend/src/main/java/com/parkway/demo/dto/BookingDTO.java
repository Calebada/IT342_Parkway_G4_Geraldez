package com.parkway.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingDTO {
    
    @JsonProperty("booking_id")
    private Long bookingId;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("parking_lot_id")
    private Long parkingLotId;
    
    @JsonProperty("parking_lot_name")
    private String parkingLotName;
    
    @JsonProperty("date_reserved")
    private LocalDate dateReserved;
    
    @JsonProperty("time_in")
    private LocalTime timeIn;
    
    @JsonProperty("time_out")
    private LocalTime timeOut;
    
    @JsonProperty("vehicle_type")
    private String vehicleType;
    
    @JsonProperty("duration")
    private Integer duration;
    
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public BookingDTO() {
    }
    
    // Constructor with all fields
    public BookingDTO(Long bookingId, Long userId, Long parkingLotId, String parkingLotName,
                      LocalDate dateReserved, LocalTime timeIn, LocalTime timeOut,
                      String vehicleType, Integer duration, BigDecimal totalPrice,
                      String status, LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.parkingLotName = parkingLotName;
        this.dateReserved = dateReserved;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.vehicleType = vehicleType;
        this.duration = duration;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
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
    
    public Long getParkingLotId() {
        return parkingLotId;
    }
    
    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }
    
    public String getParkingLotName() {
        return parkingLotName;
    }
    
    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
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
}
