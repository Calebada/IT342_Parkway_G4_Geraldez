package com.parkway.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler using @ControllerAdvice
 * 
 * PATTERN: Decorator/Middleware
 * 
 * PROBLEM SOLVED:
 * - Removes 50+ repetitive try-catch blocks from controllers and services
 * - Centralizes error response format across entire application
 * - Enables global logging and monitoring
 * 
 * BENEFITS:
 * - Single source of truth for error handling
 * - Easier to add Sentry/logging integrations
 * - Consistent API error responses for frontend
 * - Reduces code duplication by ~200 lines
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle RuntimeException (business logic errors)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        logger.error("Runtime exception occurred", e);
        
        ErrorResponse error = new ErrorResponse(
            e.getMessage(),
            "BAD_REQUEST",
            System.currentTimeMillis()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
    
    /**
     * Handle IllegalArgumentException (validation errors)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("Illegal argument provided", e);
        
        ErrorResponse error = new ErrorResponse(
            e.getMessage(),
            "INVALID_INPUT",
            System.currentTimeMillis()
        );
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
    
    /**
     * Handle generic Exception (unexpected errors)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        logger.error("Unexpected exception occurred", e);
        
        ErrorResponse error = new ErrorResponse(
            "An unexpected error occurred. Please try again later.",
            "INTERNAL_SERVER_ERROR",
            System.currentTimeMillis()
        );
        
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
