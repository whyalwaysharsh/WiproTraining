package com.pizza.billingservice.controller;

import com.pizza.billingservice.dto.BillDTO;
import com.pizza.billingservice.dto.CreateBillRequest;
import com.pizza.billingservice.dto.PaymentRequest;
import com.pizza.billingservice.entity.Bill;
import com.pizza.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing") // <--- CHANGED: Matches Gateway Route
@RequiredArgsConstructor
// REMOVED: @CrossOrigin
public class BillingController {
    
    private final BillingService billingService;
    
    @PostMapping
    public ResponseEntity<BillDTO> createBill(@RequestBody CreateBillRequest request) {
        BillDTO bill = billingService.createBill(request);
        return new ResponseEntity<>(bill, HttpStatus.CREATED);
    }
    
    @PostMapping("/{billId}/pay")
    public ResponseEntity<BillDTO> processPayment(
            @PathVariable Long billId, 
            @RequestBody PaymentRequest paymentRequest) {
        BillDTO bill = billingService.processPayment(billId, paymentRequest);
        return ResponseEntity.ok(bill);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        BillDTO bill = billingService.getBillById(id);
        return ResponseEntity.ok(bill);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<BillDTO> getBillByOrderId(@PathVariable Long orderId) {
        BillDTO bill = billingService.getBillByOrderId(orderId);
        return ResponseEntity.ok(bill);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillDTO>> getBillsByUserId(@PathVariable Long userId) {
        List<BillDTO> bills = billingService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }
    
    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<BillDTO> bills = billingService.getAllBills();
        return ResponseEntity.ok(bills);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BillDTO>> getBillsByStatus(@PathVariable Bill.PaymentStatus status) {
        List<BillDTO> bills = billingService.getBillsByPaymentStatus(status);
        return ResponseEntity.ok(bills);
    }
    
    @PostMapping("/{billId}/refund")
    public ResponseEntity<BillDTO> refundBill(@PathVariable Long billId) {
        BillDTO bill = billingService.refundBill(billId);
        return ResponseEntity.ok(bill);
    }
}