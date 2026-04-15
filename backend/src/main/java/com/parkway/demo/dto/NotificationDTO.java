package com.parkway.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class NotificationDTO {

    @JsonProperty("notification_id")
    private Long notificationId;

    @JsonProperty("recipient_user_id")
    private Long recipientUserId;

    @JsonProperty("recipient_role")
    private String recipientRole;

    @JsonProperty("booking_id")
    private Long bookingId;

    @JsonProperty("parking_lot_id")
    private Long parkingLotId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("message")
    private String message;

    @JsonProperty("is_read")
    private boolean isRead;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public NotificationDTO() {
    }

    public NotificationDTO(Long notificationId, Long recipientUserId, String recipientRole,
                           Long bookingId, Long parkingLotId, String type, String title,
                           String message, boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.recipientUserId = recipientUserId;
        this.recipientRole = recipientRole;
        this.bookingId = bookingId;
        this.parkingLotId = parkingLotId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
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
