package com.pizza.billingservice.dto;

import com.pizza.billingservice.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private Double amount;
    private Double tax;
    private Double deliveryCharge;
    private Double totalAmount;
    private Bill.PaymentStatus paymentStatus;
    private Bill.PaymentMethod paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
}