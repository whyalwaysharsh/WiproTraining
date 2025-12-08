package com.pizza.billingservice.client;

import com.pizza.billingservice.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {
    
    // CHANGED: Added "/orders" prefix
    @GetMapping("/orders/{id}")
    OrderDTO getOrderById(@PathVariable Long id);
}