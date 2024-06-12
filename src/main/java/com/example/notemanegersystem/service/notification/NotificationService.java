package com.example.notemanegersystem.service.notification;

import com.example.notemanegersystem.entity.Notification;
import com.example.notemanegersystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService{
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(Notification notification, Integer recipientUserId) {
        // Lưu thông báo vào database
        notificationRepository.save(notification);

        // Gửi thông báo qua WebSocket
        messagingTemplate.convertAndSendToUser(
                recipientUserId.toString(), "/queue/notifications", notification
        );
    }

    @Override
    public List<Notification> getNotificationsByShareRecipient(Integer recipientId) {
        List<Notification> notifications = notificationRepository.findByShareRecipientUserId(recipientId);
        return notifications;
    }
}
