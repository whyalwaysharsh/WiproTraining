package com.pizza.billingservice.repository;

import com.pizza.billingservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrderId(Long orderId);
    List<Bill> findByUserId(Long userId);
    List<Bill> findByPaymentStatus(Bill.PaymentStatus paymentStatus);
    Boolean existsByOrderId(Long orderId);
}