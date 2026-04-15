package com.parkway.demo.service;

import com.parkway.demo.dto.NotificationDTO;
import com.parkway.demo.model.Notification;
import com.parkway.demo.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(Long recipientUserId, String recipientRole,
                                           Long bookingId, Long parkingLotId,
                                           String type, String title, String message) {
        Notification notification = new Notification();
        notification.setRecipientUserId(recipientUserId);
        notification.setRecipientRole(normalizeRole(recipientRole));
        notification.setBookingId(bookingId);
        notification.setParkingLotId(parkingLotId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    public List<NotificationDTO> getNotifications(Long recipientUserId, String recipientRole) {
        String normalizedRole = normalizeRole(recipientRole);

        return notificationRepository
                .findByRecipientRoleAndRecipientUserIdOrderByCreatedAtDesc(normalizedRole, recipientUserId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Long recipientUserId, String recipientRole) {
        String normalizedRole = normalizeRole(recipientRole);
        return notificationRepository.countByRecipientRoleAndRecipientUserIdAndIsReadFalse(normalizedRole, recipientUserId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long recipientUserId, String recipientRole) {
        String normalizedRole = normalizeRole(recipientRole);

        Notification notification = notificationRepository
                .findByNotificationIdAndRecipientRoleAndRecipientUserId(notificationId, normalizedRole, recipientUserId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void markAllAsRead(Long recipientUserId, String recipientRole) {
        String normalizedRole = normalizeRole(recipientRole);
        List<Notification> notifications = notificationRepository
                .findByRecipientRoleAndRecipientUserIdOrderByCreatedAtDesc(normalizedRole, recipientUserId);

        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);

        logger.info("Marked {} notifications as read for role={} recipientId={}",
                notifications.size(), normalizedRole, recipientUserId);
    }

    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getNotificationId(),
                notification.getRecipientUserId(),
                notification.getRecipientRole(),
                notification.getBookingId(),
                notification.getParkingLotId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            throw new RuntimeException("Recipient role is required");
        }

        String normalized = role.trim().toLowerCase(Locale.ROOT);
        if (!"admin".equals(normalized) && !"user".equals(normalized)) {
            throw new RuntimeException("Invalid recipient role: " + role);
        }

        return normalized;
    }
}
