package com.pizza.orderservice.client;

import com.pizza.orderservice.dto.MenuItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "menu-service")
public interface MenuClient {
	
    @GetMapping("/menu/menu/{id}")
    MenuItemDTO getMenuItemById(@PathVariable Long id);
}