package com.parkway.demo.controller;

import com.parkway.demo.dto.LoginRequest;
import com.parkway.demo.model.Admin;
import com.parkway.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    // Register a new Admin (creates parking lot with slots)
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = adminService.register(admin);
            // Don't send password back
            savedAdmin.setPassword(null);
            return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Login Admin
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        try {
            Admin admin = adminService.login(loginRequest.getEmail(), loginRequest.getPassword());
            // Don't send password back
            admin.setPassword(null);
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
    
    // Create a new Admin (legacy endpoint)
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        try {
            // Check if email already exists
            Optional<Admin> existingAdmin = adminService.getAdminByEmail(admin.getEmail());
            if (existingAdmin.isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email already exists");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            
            // Save admin
            Admin savedAdmin = adminService.saveAdmin(admin);
            
            // Don't return password in response
            savedAdmin.setPassword(null);
            
            return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating admin: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get all Admins
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }
    
    // Get Admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("id") Long adminId) {
        Optional<Admin> admin = adminService.getAdminById(adminId);
        return admin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Update Admin
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable("id") Long staffID, 
                                              @RequestBody Admin adminDetails) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(staffID, adminDetails);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Delete Admin
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable("id") Long staffID) {
        try {
            adminService.deleteAdmin(staffID);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ONE-TIME FIX: Create parking slots for existing admins that don't have any
    @PostMapping("/create-missing-slots")
    public ResponseEntity<String> createMissingParkingSlots() {
        try {
            String result = adminService.createMissingParkingSlots();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
