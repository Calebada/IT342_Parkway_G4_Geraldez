package com.parkway.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    @JsonProperty("notification_id")
    private Long notificationId;

    @Column(name = "recipient_user_id", nullable = false)
    @JsonProperty("recipient_user_id")
    private Long recipientUserId;

    @Column(name = "recipient_role", nullable = false, length = 20)
    @JsonProperty("recipient_role")
    private String recipientRole;

    @Column(name = "booking_id")
    @JsonProperty("booking_id")
    private Long bookingId;

    @Column(name = "parking_lot_id")
    @JsonProperty("parking_lot_id")
    private Long parkingLotId;

    @Column(name = "type", nullable = false, length = 50)
    @JsonProperty("type")
    private String type;

    @Column(name = "title", nullable = false, length = 150)
    @JsonProperty("title")
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    @JsonProperty("message")
    private String message;

    @Column(name = "is_read", nullable = false)
    @JsonProperty("is_read")
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(Long recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
