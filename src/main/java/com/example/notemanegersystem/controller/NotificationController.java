package com.example.notemanegersystem.controller;

import com.example.notemanegersystem.entity.Notification;
import com.example.notemanegersystem.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping("/user/{recipientId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Integer recipientId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByShareRecipient(recipientId);
            return ResponseEntity.ok(notifications);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
