package com.parkway.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequest {
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("parking_lot_id")
    private Long parkingLotId;
    
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
    
    // Default constructor
    public BookingRequest() {
    }
    
    // Getters and Setters
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
}
