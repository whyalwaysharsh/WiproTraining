package com.pizza.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "admin-service")
public interface AdminClient {
    
    @PutMapping("/admin/revenue")
    void updateRevenue(
        // EXPLICITLY NAME THE PARAMETERS
        @RequestParam("date") LocalDate date,
        @RequestParam("amount") Double amount,
        @RequestParam("orderCount") Integer orderCount
    );
}