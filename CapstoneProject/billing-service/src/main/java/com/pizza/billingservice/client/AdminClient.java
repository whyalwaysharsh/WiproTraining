package com.pizza.billingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "admin-service")
public interface AdminClient {
    
    // CHANGED: Added "/admin" prefix
    @PutMapping("/admin/revenue")
    void updateRevenue(
        @RequestParam LocalDate date,
        @RequestParam Double amount,
        @RequestParam Integer orderCount
    );
}