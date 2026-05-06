package com.parkway.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkway.demo.dto.LoginResponse;
import com.parkway.demo.model.User;
import com.parkway.demo.service.UserService;

@RestController
@RequestMapping("/api/oauth2")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000"})
public class OAuth2Controller {
    
    @Autowired
    private UserService userService;
    
    // Endpoint for frontend to send Google user info after OAuth2 authentication
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String firstName = request.get("firstName");
            String lastName = request.get("lastName");
            
            if (email == null || email.isEmpty()) {
                return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
            }
            
            // Try to find user by email, if not found create a new user
            User user = userService.getUserByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setFirstname(firstName != null ? firstName : "");
                        newUser.setLastname(lastName != null ? lastName : "");
                        newUser.setRole("USER");
                        newUser.setPassword(""); // OAuth2 users don't have passwords
                        
                        return userService.saveOAuth2User(newUser);
                    });
            
            LoginResponse response = new LoginResponse(
                user.getUserID(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                "OAuth2 login successful"
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error during OAuth2 authentication: " + e.getMessage());
            error.put("errorCode", "OAUTH2_AUTH_ERROR");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
