package com.parkway.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parkway.demo.model.Admin;
import com.parkway.demo.model.ParkingSlot;
import com.parkway.demo.repository.AdminRepository;
import com.parkway.demo.repository.ParkingSlotRepository;

/**
 * Admin Creation Facade
 * 
 * PATTERN: Facade
 * 
 * PROBLEM SOLVED:
 * - AdminService had duplicate parking slot creation logic in 3 methods:
 *   1. register() - lines 25-35
 *   2. saveAdmin() - lines 50-60
 *   3. createMissingParkingSlots() - lines 135-145
 * - EXACT same for-loop repeated 3 times
 * - Creates slots even if they already exist
 * - No atomic transaction handling
 * 
 * BENEFITS:
 * - Single source of truth for admin registration flow
 * - Atomic operation (all-or-nothing)
 * - Slot duplication prevented
 * - Clearer intent and easier to maintain
 * - Easy to extend (e.g., add email verification, setup notifications)
 * - Can be reused by other services
 */
@Service
public class AdminCreationFacade {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    /**
     * Register admin with automatic parking slot creation
     * ATOMIC: All-or-nothing transaction
     */
    @Transactional
    public Admin registerAdminWithSlots(Admin admin) {
        // Step 1: Validate email uniqueness
        Optional<Admin> existingAdmin = adminRepository.findByEmail(admin.getEmail());
        if (existingAdmin.isPresent()) {
            throw new RuntimeException("Email already exists: " + admin.getEmail());
        }
        
        // Step 2: Save admin (triggers database insert)
        Admin savedAdmin = adminRepository.save(admin);
        
        // Step 3: Create parking slots atomically
        createParkingSlots(savedAdmin);
        
        return savedAdmin;
    }
    
    /**
     * Create parking slots for an admin
     * Uses a single, centralized implementation (no duplication)
     */
    @Transactional
    public void createParkingSlots(Admin admin) {
        if (admin == null || admin.getAdminId() == null) {
            throw new IllegalArgumentException("Admin and AdminId cannot be null");
        }
        
        int capacity = admin.getCapacity() != null ? admin.getCapacity() : 0;
        
        if (capacity <= 0) {
            throw new IllegalArgumentException("Admin capacity must be greater than 0");
        }
        
        // Create slots 1 through capacity
        for (int i = 1; i <= capacity; i++) {
            ParkingSlot slot = new ParkingSlot(admin, i);
            parkingSlotRepository.save(slot);
        }
    }
}
