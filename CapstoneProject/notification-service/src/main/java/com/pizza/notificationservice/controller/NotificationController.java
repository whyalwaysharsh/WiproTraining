package com.pizza.notificationservice.controller;

import com.pizza.notificationservice.dto.NotificationDTO;
import com.pizza.notificationservice.dto.SendNotificationRequest;
import com.pizza.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications") 
@RequiredArgsConstructor
// REMOVED: @CrossOrigin
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping("/send")
    public ResponseEntity<NotificationDTO> sendNotification(@RequestBody SendNotificationRequest request) {
        NotificationDTO notification = notificationService.sendNotification(request);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }
    
    // Alternative endpoint for Map-based requests (Used by OrderService Feign Client)
    @PostMapping("/send-map")
    public ResponseEntity<NotificationDTO> sendNotificationMap(@RequestBody Map<String, String> request) {
        SendNotificationRequest notifRequest = new SendNotificationRequest();
        notifRequest.setUserId(Long.parseLong(request.get("userId")));
        notifRequest.setMessage(request.get("message"));
        notifRequest.setType(request.get("type"));
        
        NotificationDTO notification = notificationService.sendNotification(notifRequest);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("unreadCount", count);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}