package com.parkway.demo.service;

import com.parkway.demo.dto.VehicleDTO;
import com.parkway.demo.dto.VehicleRequest;
import com.parkway.demo.dto.VehicleWithUserDTO;
import com.parkway.demo.model.User;
import com.parkway.demo.model.Vehicle;
import com.parkway.demo.repository.UserRepository;
import com.parkway.demo.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Add a new vehicle for a user
     */
    @Transactional
    public Vehicle addVehicle(VehicleRequest request) {
        try {
            logger.info("Attempting to add vehicle for user ID: {}", request.getUserId());
            
            // Check if user exists
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
            
            // Check if plate number already exists
            if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
                logger.error("Plate number already exists: {}", request.getPlateNumber());
                throw new RuntimeException("Plate number already exists: " + request.getPlateNumber());
            }
            
            // Create new vehicle
            Vehicle vehicle = new Vehicle();
            vehicle.setUser(user);
            vehicle.setPlateNumber(request.getPlateNumber());
            vehicle.setModel(request.getModel());
            vehicle.setVehicleType(request.getVehicleType());
            
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            logger.info("Vehicle added successfully with ID: {}", savedVehicle.getVehicleID());
            
            return savedVehicle;
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while adding vehicle: {}", e.getMessage());
            throw new RuntimeException("Plate number already exists: " + request.getPlateNumber());
        } catch (Exception e) {
            logger.error("Error adding vehicle: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Get vehicle by user ID
     */
    public VehicleDTO getVehicleByUserId(Long userId) {
        try {
            logger.info("Fetching vehicle for user ID: {}", userId);
            
            Vehicle vehicle = vehicleRepository.findByUser_UserID(userId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found for user id: " + userId));
            
            return convertToDTO(vehicle);
            
        } catch (Exception e) {
            logger.error("Error fetching vehicle for user ID {}: {}", userId, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get all vehicles with user details (Admin)
     */
    public List<VehicleWithUserDTO> getAllVehiclesWithUserDetails() {
        try {
            logger.info("Fetching all vehicles with user details");
            
            List<Vehicle> vehicles = vehicleRepository.findAll();
            
            return vehicles.stream()
                    .map(this::convertToWithUserDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching all vehicles: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Update vehicle information
     */
    @Transactional
    public Vehicle updateVehicle(Long vehicleId, VehicleRequest request) {
        try {
            logger.info("Attempting to update vehicle ID: {}", vehicleId);
            
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
            
            // Check if new plate number already exists (excluding current vehicle)
            if (!vehicle.getPlateNumber().equals(request.getPlateNumber())) {
                if (vehicleRepository.existsByPlateNumberAndNotVehicleId(request.getPlateNumber(), vehicleId)) {
                    logger.error("Plate number already exists: {}", request.getPlateNumber());
                    throw new RuntimeException("Plate number already exists: " + request.getPlateNumber());
                }
            }
            
            // Update vehicle details
            vehicle.setPlateNumber(request.getPlateNumber());
            vehicle.setModel(request.getModel());
            vehicle.setVehicleType(request.getVehicleType());
            
            Vehicle updatedVehicle = vehicleRepository.save(vehicle);
            logger.info("Vehicle updated successfully: {}", vehicleId);
            
            return updatedVehicle;
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation while updating vehicle: {}", e.getMessage());
            throw new RuntimeException("Plate number already exists: " + request.getPlateNumber());
        } catch (Exception e) {
            logger.error("Error updating vehicle: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Delete vehicle by ID
     */
    @Transactional
    public void deleteVehicle(Long vehicleId) {
        try {
            logger.info("Attempting to delete vehicle ID: {}", vehicleId);
            
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));
            
            vehicleRepository.delete(vehicle);
            logger.info("Vehicle deleted successfully: {}", vehicleId);
            
        } catch (Exception e) {
            logger.error("Error deleting vehicle: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Convert Vehicle entity to VehicleDTO
     */
    private VehicleDTO convertToDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getVehicleID(),
                vehicle.getUser().getUserID(),
                vehicle.getPlateNumber(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getCreatedAt()
        );
    }
    
    /**
     * Convert Vehicle entity to VehicleWithUserDTO
     */
    private VehicleWithUserDTO convertToWithUserDTO(Vehicle vehicle) {
        return new VehicleWithUserDTO(
                vehicle.getVehicleID(),
                vehicle.getUser().getUserID(),
                vehicle.getPlateNumber(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getCreatedAt(),
                vehicle.getUser().getFirstname(),
                vehicle.getUser().getLastname()
        );
    }
}
