package com.pizza.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponse {
    private String token;
    private String type = "Bearer";
    private AdminDTO admin;
    
    public AdminLoginResponse(String token, AdminDTO admin) {
        this.token = token;
        this.admin = admin;
    }
}