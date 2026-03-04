package com.parkway.demo.controller;

import com.parkway.demo.dto.LoginRequest;
import com.parkway.demo.dto.LoginResponse;
import com.parkway.demo.dto.UserDTO;
import com.parkway.demo.model.User;
import com.parkway.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Register/Create a new User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            UserDTO userDTO = convertToDTO(savedUser);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            LoginResponse response = new LoginResponse(
                user.getUserID(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                "Login successful"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    
    // Get all Users (without password)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
    
    // Get User by ID (without password)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userID) {
        Optional<User> user = userService.getUserById(userID);
        if (user.isPresent()) {
            UserDTO userDTO = convertToDTO(user.get());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    // Update User
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userID, 
                                        @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(userID, userDetails);
            UserDTO userDTO = convertToDTO(updatedUser);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userID) {
        try {
            userService.deleteUser(userID);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // Helper method to convert User to UserDTO (excluding password)
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getUserID(),
            user.getFirstname(),
            user.getLastname(),
            user.getEmail()
        );
    }
}
