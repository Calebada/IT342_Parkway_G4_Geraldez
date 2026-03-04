package com.parkway.demo.controller;

import com.parkway.demo.dto.BookingDTO;
import com.parkway.demo.dto.BookingRequest;
import com.parkway.demo.dto.BookingResponse;
import com.parkway.demo.model.Booking;
import com.parkway.demo.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class BookingController {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * GET /api/bookings/user/:userId
     * Fetch all bookings for a specific user
     * Returns empty array [] if no bookings found (NOT 404)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable("userId") Long userId) {
        try {
            logger.info("GET /api/bookings/user/{} - Fetching user bookings", userId);
            
            List<BookingDTO> bookings = bookingService.getUserBookings(userId);
            
            // Always return 200 OK with empty array if no bookings found
            return new ResponseEntity<>(bookings, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error fetching bookings for user {}: {}", userId, e.getMessage());
            // Return empty array even on error to prevent frontend issues
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }
    
    /**
     * POST /api/bookings
     * Create a new booking
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            logger.info("POST /api/bookings - Creating new booking");
            
            Booking savedBooking = bookingService.createBooking(request);
            BookingResponse response = new BookingResponse(
                    "Booking created successfully",
                    savedBooking.getBookingId()
            );
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            logger.error("Error creating booking: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * GET /api/bookings/admin/:userId
     * Get all bookings for admin's parking lot
     * Returns empty array [] if no bookings found
     */
    @GetMapping("/admin/{userId}")
    public ResponseEntity<List<com.parkway.demo.dto.AdminBookingDTO>> getAdminBookings(@PathVariable("userId") Long userId) {
        try {
            logger.info("GET /api/bookings/admin/{} - Fetching admin bookings", userId);
            
            List<com.parkway.demo.dto.AdminBookingDTO> bookings = bookingService.getAdminBookings(userId);
            
            // Always return 200 OK with empty array if no bookings found
            return new ResponseEntity<>(bookings, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error fetching admin bookings for user {}: {}", userId, e.getMessage());
            // Return empty array even on error to prevent frontend issues
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }
    
    /**
     * PUT /api/bookings/:bookingId/confirm
     * Confirm a booking (change status from pending to confirmed)
     */
    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable("bookingId") Long bookingId) {
        try {
            logger.info("PUT /api/bookings/{}/confirm - Confirming booking", bookingId);
            
            bookingService.confirmBooking(bookingId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking confirmed successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error confirming booking {}: {}", bookingId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * DELETE /api/bookings/:bookingId
     * Delete a booking and free up parking slot if confirmed
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable("bookingId") Long bookingId) {
        try {
            logger.info("DELETE /api/bookings/{} - Deleting booking", bookingId);
            
            bookingService.deleteBooking(bookingId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Booking deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error deleting booking {}: {}", bookingId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
