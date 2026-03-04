package com.parkway.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("bookingId")
    private Long bookingId;
    
    // Default constructor
    public BookingResponse() {
    }
    
    // Constructor with fields
    public BookingResponse(String message, Long bookingId) {
        this.message = message;
        this.bookingId = bookingId;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }
}
