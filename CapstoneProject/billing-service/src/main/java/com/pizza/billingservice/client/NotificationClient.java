package com.pizza.billingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service")
public interface NotificationClient {
    
    // CHANGED: Use the map-specific endpoint
    @PostMapping("/notifications/send-map")
    void sendNotification(@RequestBody Map<String, String> notification);
}