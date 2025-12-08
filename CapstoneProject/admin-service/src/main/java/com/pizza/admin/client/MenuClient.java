package com.pizza.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "menu-service")
public interface MenuClient {
    
    @PostMapping("/menu/add")
    Object addMenuItem(@RequestBody Object item);
}