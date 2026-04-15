package com.parkway.demo.repository;

import com.parkway.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientRoleAndRecipientUserIdOrderByCreatedAtDesc(String recipientRole, Long recipientUserId);

    long countByRecipientRoleAndRecipientUserIdAndIsReadFalse(String recipientRole, Long recipientUserId);

    Optional<Notification> findByNotificationIdAndRecipientRoleAndRecipientUserId(Long notificationId, String recipientRole, Long recipientUserId);
}
