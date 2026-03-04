package com.parkway.demo.controller;

import com.parkway.demo.model.Admin;
import com.parkway.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ParkingLotController {
    
    @Autowired
    private AdminService adminService;
    
    // Get all parking lots
    @GetMapping("/parking-lots")
    public ResponseEntity<?> getAllParkingLots() {
        try {
            List<Admin> parkingLots = adminService.getAllAdmins();
            return ResponseEntity.ok(parkingLots);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
