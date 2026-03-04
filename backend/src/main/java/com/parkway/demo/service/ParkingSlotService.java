package com.parkway.demo.service;

import com.parkway.demo.dto.ParkingSlotDTO;
import com.parkway.demo.model.ParkingSlot;
import com.parkway.demo.repository.ParkingSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParkingSlotService {
    
    private static final Logger logger = LoggerFactory.getLogger(ParkingSlotService.class);
    
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    /**
     * Get all parking slots for a specific parking lot
     */
    public List<ParkingSlotDTO> getParkingSlots(Long parkingLotId) {
        try {
            logger.info("Fetching parking slots for parking lot ID: {}", parkingLotId);
            
            List<ParkingSlot> slots = parkingSlotRepository
                    .findByAdmin_AdminIdOrderBySlotNumberAsc(parkingLotId);
            
            logger.info("Found {} parking slot(s)", slots.size());
            
            return slots.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error fetching parking slots: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching parking slots: " + e.getMessage());
        }
    }
    
    /**
     * Update parking slot status and return updated slot
     */
    @Transactional
    public ParkingSlotDTO updateSlotStatus(Long slotId, String status) {
        try {
            logger.info("Updating parking slot {} to status: {}", slotId, status);
            
            ParkingSlot slot = parkingSlotRepository.findById(slotId)
                    .orElseThrow(() -> new RuntimeException("Parking slot not found with id: " + slotId));
            
            slot.setStatus(status);
            ParkingSlot updatedSlot = parkingSlotRepository.save(slot);
            
            logger.info("Parking slot updated successfully to status: {}", status);
            
            return convertToDTO(updatedSlot);
            
        } catch (Exception e) {
            logger.error("Error updating parking slot: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Convert ParkingSlot entity to ParkingSlotDTO
     */
    private ParkingSlotDTO convertToDTO(ParkingSlot slot) {
        return new ParkingSlotDTO(
                slot.getSlotId(),
                slot.getAdmin().getAdminId(),
                slot.getSlotNumber(),
                slot.getStatus()
        );
    }
    
    /**
     * Check if parking lot has available slots
     */
    public boolean hasAvailableSlots(Long parkingLotId) {
        try {
            logger.info("Checking availability for parking lot ID: {}", parkingLotId);
            
            List<ParkingSlot> slots = parkingSlotRepository
                    .findByAdmin_AdminIdOrderBySlotNumberAsc(parkingLotId);
            
            if (slots.isEmpty()) {
                logger.warn("No slots found for parking lot ID: {}", parkingLotId);
                return false;
            }
            
            long occupiedCount = slots.stream()
                    .filter(slot -> "occupied".equals(slot.getStatus()))
                    .count();
            
            boolean hasAvailable = occupiedCount < slots.size();
            logger.info("Parking lot {}: {}/{} occupied. Available: {}", 
                       parkingLotId, occupiedCount, slots.size(), hasAvailable);
            
            return hasAvailable;
            
        } catch (Exception e) {
            logger.error("Error checking parking lot availability: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Get parking lot occupancy info
     */
    public Map<String, Long> getOccupancyInfo(Long parkingLotId) {
        try {
            List<ParkingSlot> slots = parkingSlotRepository
                    .findByAdmin_AdminIdOrderBySlotNumberAsc(parkingLotId);
            
            long totalSlots = slots.size();
            long occupiedSlots = slots.stream()
                    .filter(slot -> "occupied".equals(slot.getStatus()))
                    .count();
            
            Map<String, Long> occupancy = new java.util.HashMap<>();
            occupancy.put("total", totalSlots);
            occupancy.put("occupied", occupiedSlots);
            occupancy.put("available", totalSlots - occupiedSlots);
            
            return occupancy;
            
        } catch (Exception e) {
            logger.error("Error getting occupancy info: {}", e.getMessage(), e);
            return java.util.Collections.emptyMap();
        }
    }
}
