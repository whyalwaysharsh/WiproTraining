package com.pizza.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private String deliveryAddress;
    private String phoneNumber;
    private String specialInstructions;
}