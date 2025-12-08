package com.pizza.billingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBillRequest {
    private Long orderId;
    private Long userId;
    private Double amount;
    private Double tax;
    private Double deliveryCharge;
}