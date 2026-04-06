package com.parkway.demo.exception;

/**
 * Standardized Error Response DTO
 * Used by GlobalExceptionHandler to return consistent error format
 */
public class ErrorResponse {
    private String message;
    private String errorCode;
    private long timestamp;
    
    public ErrorResponse(String message, String errorCode, long timestamp) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getMessage() {
        return message;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
