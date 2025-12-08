package com.pizza.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementDTO {
    private Long userId;
    private String email;
    private String fullName;
    private Boolean active;
    private String action; 
}