package com.parkway.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkway.demo.dto.NotificationDTO;
import com.parkway.demo.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{role}/{recipientId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable("role") String role,
                                                                  @PathVariable("recipientId") Long recipientId) {
        try {
            return new ResponseEntity<>(notificationService.getNotifications(recipientId, role), HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error fetching notifications: {}", e.getMessage());
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }
    }

    @GetMapping("/{role}/{recipientId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable("role") String role,
                                                            @PathVariable("recipientId") Long recipientId) {
        Map<String, Long> response = new HashMap<>();
        try {
            response.put("unread_count", notificationService.getUnreadCount(recipientId, role));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error fetching unread notification count: {}", e.getMessage());
            response.put("unread_count", 0L);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PutMapping("/{role}/{recipientId}/{notificationId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable("role") String role,
                                                           @PathVariable("recipientId") Long recipientId,
                                                           @PathVariable("notificationId") Long notificationId) {
        Map<String, String> response = new HashMap<>();
        try {
            notificationService.markAsRead(notificationId, recipientId, role);
            response.put("message", "Notification marked as read");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{role}/{recipientId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable("role") String role,
                                                              @PathVariable("recipientId") Long recipientId) {
        Map<String, String> response = new HashMap<>();
        try {
            notificationService.markAllAsRead(recipientId, role);
            response.put("message", "All notifications marked as read");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
