package com.pizza.billingservice.dto;

import com.pizza.billingservice.entity.Bill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Bill.PaymentMethod paymentMethod;
    private String transactionId;
}