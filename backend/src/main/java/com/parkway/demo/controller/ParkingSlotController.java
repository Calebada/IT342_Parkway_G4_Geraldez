package com.parkway.demo.controller;

import com.parkway.demo.dto.ParkingSlotDTO;
import com.parkway.demo.service.ParkingSlotService;
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
@RequestMapping("/api/parking-slots")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ParkingSlotController {
    
    private static final Logger logger = LoggerFactory.getLogger(ParkingSlotController.class);
    
    @Autowired
    private ParkingSlotService parkingSlotService;
    
    @Autowired
    private com.parkway.demo.service.AdminService adminService;
    
    /**
     * POST /api/parking-slots/initialize
     * One-time initialization to create slots for existing parking lots
     */
    @PostMapping("/initialize")
    public ResponseEntity<?> initializeParkingSlots() {
        try {
            logger.info("POST /api/parking-slots/initialize - Initializing parking slots");
            String result = adminService.createMissingParkingSlots();
            Map<String, String> response = new HashMap<>();
            response.put("message", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error initializing parking slots: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/parking-slots/:parkingLotId
     * Get all parking slots for a specific parking lot
     */
    @GetMapping("/{parkingLotId}")
    public ResponseEntity<List<ParkingSlotDTO>> getParkingSlots(@PathVariable("parkingLotId") Long parkingLotId) {
        try {
            logger.info("GET /api/parking-slots/{} - Fetching parking slots", parkingLotId);
            
            List<ParkingSlotDTO> slots = parkingSlotService.getParkingSlots(parkingLotId);
            
            return new ResponseEntity<>(slots, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error fetching parking slots: {}", e.getMessage());
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }
    
    /**
     * GET /api/parking-slots/:parkingLotId/availability
     * Check if parking lot has available slots
     */
    @GetMapping("/{parkingLotId}/availability")
    public ResponseEntity<?> checkAvailability(@PathVariable("parkingLotId") Long parkingLotId) {
        try {
            logger.info("GET /api/parking-slots/{}/availability - Checking availability", parkingLotId);
            
            Map<String, Object> availability = new HashMap<>();
            boolean hasAvailable = parkingSlotService.hasAvailableSlots(parkingLotId);
            Map<String, Long> occupancy = parkingSlotService.getOccupancyInfo(parkingLotId);
            
            availability.put("hasAvailable", hasAvailable);
            availability.put("occupancy", occupancy);
            
            return new ResponseEntity<>(availability, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error checking parking availability: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error checking parking availability: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * PUT /api/parking-slots/:slotId
     * Update parking slot status and return updated slot
     */
    @PutMapping("/{slotId}")
    public ResponseEntity<?> updateSlotStatus(@PathVariable("slotId") Long slotId,
                                              @RequestBody Map<String, String> request) {
        try {
            logger.info("PUT /api/parking-slots/{} - Updating slot status", slotId);
            
            String status = request.get("status");
            if (status == null || (!status.equals("vacant") && !status.equals("occupied"))) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Invalid status. Must be 'vacant' or 'occupied'");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            
            ParkingSlotDTO updatedSlot = parkingSlotService.updateSlotStatus(slotId, status);
            
            return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error updating slot status: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to update parking slot: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
