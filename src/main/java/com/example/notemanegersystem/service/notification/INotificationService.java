package com.example.notemanegersystem.service.notification;

import com.example.notemanegersystem.entity.Notification;

import java.util.List;


public interface INotificationService {
    void sendNotificationToUser(Notification notification, Integer recipientUserId);
    List<Notification> getNotificationsByShareRecipient(Integer recipientId);
}
