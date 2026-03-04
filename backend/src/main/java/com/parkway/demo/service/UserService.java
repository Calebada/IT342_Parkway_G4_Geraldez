package com.parkway.demo.service;

import com.parkway.demo.model.User;
import com.parkway.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Create User with hashed password
    public User saveUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get User by ID
    public Optional<User> getUserById(Long userID) {
        return userRepository.findById(userID);
    }
    
    // Get User by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Update User
    public User updateUser(Long userID, User userDetails) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userID));
        
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        
        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(userDetails.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }
        
        // Only update password if a new one is provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    // Delete User
    public void deleteUser(Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userID));
        userRepository.delete(user);
    }
    
    // Verify user credentials for login
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    // Login method
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        return user;
    }
}
