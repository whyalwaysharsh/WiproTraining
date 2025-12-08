package com.pizza.billingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String status;
    private String deliveryAddress;
    private String phoneNumber;
    private LocalDateTime createdAt;
}