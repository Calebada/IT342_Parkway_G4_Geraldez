package com.parkway.demo.service;

import com.parkway.demo.model.Admin;
import com.parkway.demo.model.ParkingSlot;
import com.parkway.demo.repository.AdminRepository;
import com.parkway.demo.repository.ParkingSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    // Register new Admin (creates parking lot with slots)
    @Transactional
    public Admin register(Admin admin) {
        // Check if email already exists
        Optional<Admin> existingAdmin = adminRepository.findByEmail(admin.getEmail());
        if (existingAdmin.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        Admin savedAdmin = adminRepository.save(admin);
        
        // Automatically create parking slots based on capacity
        if (admin.getCapacity() != null && admin.getCapacity() > 0) {
            logger.info("Creating {} parking slots for parking lot: {}", 
                       admin.getCapacity(), savedAdmin.getAdminId());
            
            for (int i = 1; i <= admin.getCapacity(); i++) {
                ParkingSlot slot = new ParkingSlot(savedAdmin, i);
                parkingSlotRepository.save(slot);
            }
            
            logger.info("Successfully created {} parking slots", admin.getCapacity());
        }
        
        return savedAdmin;
    }
    
    // Login Admin
    public Admin login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }
        
        return admin;
    }
    
    // Create or Update Admin (legacy method)
    @Transactional
    public Admin saveAdmin(Admin admin) {
        Admin savedAdmin = adminRepository.save(admin);
        
        // Automatically create parking slots based on capacity
        if (admin.getCapacity() != null && admin.getCapacity() > 0) {
            logger.info("Creating {} parking slots for parking lot: {}", 
                       admin.getCapacity(), savedAdmin.getStaffID());
            
            for (int i = 1; i <= admin.getCapacity(); i++) {
                ParkingSlot slot = new ParkingSlot(savedAdmin, i);
                parkingSlotRepository.save(slot);
            }
            
            logger.info("Successfully created {} parking slots", admin.getCapacity());
        }
        
        return savedAdmin;
    }
    
    // Get all Admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    // Get Admin by staff ID
    public Optional<Admin> getAdminById(Long staffID) {
        return adminRepository.findById(staffID);
    }
    
    // Get Admin by email
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    
    // Update Admin
    public Admin updateAdmin(Long adminId, Admin adminDetails) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (adminDetails.getEmail() != null) admin.setEmail(adminDetails.getEmail());
        if (adminDetails.getFirstname() != null) admin.setFirstname(adminDetails.getFirstname());
        if (adminDetails.getLastname() != null) admin.setLastname(adminDetails.getLastname());
        if (adminDetails.getParkingLotName() != null) admin.setParkingLotName(adminDetails.getParkingLotName());
        if (adminDetails.getCapacity() != null) admin.setCapacity(adminDetails.getCapacity());
        if (adminDetails.getPrice() != null) admin.setPrice(adminDetails.getPrice());
        if (adminDetails.getLocation() != null) admin.setLocation(adminDetails.getLocation());
        
        return adminRepository.save(admin);
    }
    
    // Delete Admin
    public void deleteAdmin(Long staffID) {
        Admin admin = adminRepository.findById(staffID)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + staffID));
        adminRepository.delete(admin);
    }
    
    /**
     * Create parking slots for existing admins that don't have any
     * This is a one-time fix for existing parking lots
     */
    @Transactional
    public String createMissingParkingSlots() {
        logger.info("Starting to create missing parking slots for existing admins");
        
        List<Admin> allAdmins = adminRepository.findAll();
        int adminCount = 0;
        int slotCount = 0;
        
        for (Admin admin : allAdmins) {
            // Check if slots already exist
            List<ParkingSlot> existingSlots = parkingSlotRepository.findByAdmin(admin);
            
            if (existingSlots.isEmpty() && admin.getCapacity() != null && admin.getCapacity() > 0) {
                logger.info("Creating {} parking slots for admin {}", admin.getCapacity(), admin.getStaffID());
                
                // Create slots
                for (int i = 1; i <= admin.getCapacity(); i++) {
                    ParkingSlot slot = new ParkingSlot(admin, i);
                    parkingSlotRepository.save(slot);
                    slotCount++;
                }
                adminCount++;
            }
        }
        
        String message = String.format("Created %d parking slots for %d admin(s)", slotCount, adminCount);
        logger.info(message);
        return message;
    }
}
