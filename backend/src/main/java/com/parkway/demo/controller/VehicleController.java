package com.parkway.demo.controller;

import com.parkway.demo.dto.VehicleDTO;
import com.parkway.demo.dto.VehicleRequest;
import com.parkway.demo.dto.VehicleResponse;
import com.parkway.demo.dto.VehicleWithUserDTO;
import com.parkway.demo.model.Vehicle;
import com.parkway.demo.service.VehicleService;
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
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class VehicleController {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    
    @Autowired
    private VehicleService vehicleService;
    
    /**
     * 1. Add Vehicle - POST /api/vehicles
     * Accepts: { user_id, plate_number, model, vehicle_type }
     * Returns: { vehicle_id, message }
     */
    @PostMapping
    public ResponseEntity<?> addVehicle(@RequestBody VehicleRequest request) {
        try {
            logger.info("POST /api/vehicles - Adding vehicle for user: {}", request.getUserId());
            
            Vehicle savedVehicle = vehicleService.addVehicle(request);
            VehicleResponse response = new VehicleResponse(
                    savedVehicle.getVehicleID(),
                    "Vehicle added successfully"
            );
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (RuntimeException e) {
            logger.error("Error adding vehicle: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 2. Get User Vehicle - GET /api/vehicles/user/:userId
     * Returns: Single vehicle object or 404
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserVehicle(@PathVariable("userId") Long userId) {
        try {
            logger.info("GET /api/vehicles/user/{} - Fetching vehicle", userId);
            
            VehicleDTO vehicle = vehicleService.getVehicleByUserId(userId);
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error fetching vehicle for user {}: {}", userId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 3. Update Vehicle - PUT /api/vehicles/:vehicleId
     * Accepts: { plate_number, model, vehicle_type }
     * Returns: { message }
     */
    @PutMapping("/{vehicleId}")
    public ResponseEntity<?> updateVehicle(@PathVariable("vehicleId") Long vehicleId,
                                           @RequestBody VehicleRequest request) {
        try {
            logger.info("PUT /api/vehicles/{} - Updating vehicle", vehicleId);
            
            vehicleService.updateVehicle(vehicleId, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Vehicle updated successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error updating vehicle {}: {}", vehicleId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            
            if (e.getMessage().contains("not found")) {
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * 4. Delete Vehicle - DELETE /api/vehicles/:vehicleId
     * Returns: { message }
     */
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable("vehicleId") Long vehicleId) {
        try {
            logger.info("DELETE /api/vehicles/{} - Deleting vehicle", vehicleId);
            
            vehicleService.deleteVehicle(vehicleId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Vehicle deleted successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error deleting vehicle {}: {}", vehicleId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * 5. Get All Vehicles (Admin) - GET /api/vehicles
     * Returns: Array of vehicles with user details
     */
    @GetMapping
    public ResponseEntity<?> getAllVehicles() {
        try {
            logger.info("GET /api/vehicles - Fetching all vehicles with user details");
            
            List<VehicleWithUserDTO> vehicles = vehicleService.getAllVehiclesWithUserDetails();
            return new ResponseEntity<>(vehicles, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            logger.error("Error fetching all vehicles: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
