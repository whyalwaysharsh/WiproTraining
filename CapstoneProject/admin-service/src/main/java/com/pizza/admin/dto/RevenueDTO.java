package com.pizza.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO {
    private Long id;
    private LocalDate date;
    private Double amount;
    private Integer orderCount;
    private String description;
}