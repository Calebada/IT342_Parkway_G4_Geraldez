package com.parkway.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkingSlotDTO {
    
    @JsonProperty("slot_id")
    private Long slotId;
    
    @JsonProperty("parking_lot_id")
    private Long parkingLotId;
    
    @JsonProperty("slot_number")
    private Integer slotNumber;
    
    @JsonProperty("status")
    private String status;
    
    // Default constructor
    public ParkingSlotDTO() {
    }
    
    // Constructor
    public ParkingSlotDTO(Long slotId, Long parkingLotId, Integer slotNumber, String status) {
        this.slotId = slotId;
        this.parkingLotId = parkingLotId;
        this.slotNumber = slotNumber;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getSlotId() {
        return slotId;
    }
    
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }
    
    public Long getParkingLotId() {
        return parkingLotId;
    }
    
    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
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
}
