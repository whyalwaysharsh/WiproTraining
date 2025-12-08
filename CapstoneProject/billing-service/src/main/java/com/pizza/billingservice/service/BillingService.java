package com.pizza.billingservice.service;

import com.pizza.billingservice.client.AdminClient;
import com.pizza.billingservice.client.NotificationClient;
import com.pizza.billingservice.client.OrderClient;
import com.pizza.billingservice.dto.BillDTO;
import com.pizza.billingservice.dto.CreateBillRequest;
import com.pizza.billingservice.dto.OrderDTO;
import com.pizza.billingservice.dto.PaymentRequest;
import com.pizza.billingservice.entity.Bill;
import com.pizza.billingservice.exception.BillNotFoundException;
import com.pizza.billingservice.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingService {
    
    private final BillRepository billRepository;
    private final OrderClient orderClient;
    private final AdminClient adminClient;
    private final NotificationClient notificationClient;
    
    @Transactional
    public BillDTO createBill(CreateBillRequest request) {
        if (billRepository.existsByOrderId(request.getOrderId())) {
            throw new IllegalStateException("Bill already exists for order: " + request.getOrderId());
        }
        
        OrderDTO order = orderClient.getOrderById(request.getOrderId());
        
        Bill bill = new Bill();
        bill.setOrderId(request.getOrderId());
        bill.setUserId(request.getUserId());
        bill.setAmount(request.getAmount());
        bill.setTax(request.getTax());
        bill.setDeliveryCharge(request.getDeliveryCharge());
        bill.setTotalAmount(request.getAmount() + request.getTax() + request.getDeliveryCharge());
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        
        Bill saved = billRepository.save(bill);
        
        // Send notification
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("userId", String.valueOf(saved.getUserId()));
            notification.put("message", "Bill generated for order #" + saved.getOrderId() + ". Total: ₹" + saved.getTotalAmount());
            notification.put("type", "BILL_GENERATED");
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
        
        return convertToDTO(saved);
    }
    
    @Transactional
    public BillDTO processPayment(Long billId, PaymentRequest paymentRequest) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with id: " + billId));
        
        if (bill.getPaymentStatus() == Bill.PaymentStatus.PAID) {
            throw new IllegalStateException("Bill is already paid");
        }
        
        // Simulate payment processing
        boolean paymentSuccess = processPaymentWithGateway(paymentRequest);
        
        if (paymentSuccess) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
            bill.setPaymentMethod(paymentRequest.getPaymentMethod());
            bill.setTransactionId(paymentRequest.getTransactionId());
            bill.setPaidAt(LocalDateTime.now());
            
            Bill updated = billRepository.save(bill);
            
            // Update revenue in admin service
            try {
                adminClient.updateRevenue(LocalDate.now(), updated.getTotalAmount(), 1);
            } catch (Exception e) {
                System.err.println("Failed to update revenue: " + e.getMessage());
            }
            
            // Send notification
            try {
                Map<String, String> notification = new HashMap<>();
                notification.put("userId", String.valueOf(updated.getUserId()));
                notification.put("message", "Payment successful for order #" + updated.getOrderId() + ". Amount: ₹" + updated.getTotalAmount());
                notification.put("type", "PAYMENT_SUCCESS");
                notificationClient.sendNotification(notification);
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
            
            return convertToDTO(updated);
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.FAILED);
            billRepository.save(bill);
            
            // Send notification
            try {
                Map<String, String> notification = new HashMap<>();
                notification.put("userId", String.valueOf(bill.getUserId()));
                notification.put("message", "Payment failed for order #" + bill.getOrderId() + ". Please try again.");
                notification.put("type", "PAYMENT_FAILED");
                notificationClient.sendNotification(notification);
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
            
            throw new IllegalStateException("Payment processing failed");
        }
    }
    
    private boolean processPaymentWithGateway(PaymentRequest paymentRequest) {
        // Simulate payment gateway processing
        // In real implementation, integrate with actual payment gateway
        return true;
    }
    
    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with id: " + id));
        return convertToDTO(bill);
    }
    
    public BillDTO getBillByOrderId(Long orderId) {
        Bill bill = billRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found for order: " + orderId));
        return convertToDTO(bill);
    }
    
    public List<BillDTO> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BillDTO> getBillsByPaymentStatus(Bill.PaymentStatus status) {
        return billRepository.findByPaymentStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BillDTO refundBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with id: " + billId));
        
        if (bill.getPaymentStatus() != Bill.PaymentStatus.PAID) {
            throw new IllegalStateException("Can only refund paid bills");
        }
        
        bill.setPaymentStatus(Bill.PaymentStatus.REFUNDED);
        Bill updated = billRepository.save(bill);
        
        // Update revenue (subtract)
        try {
            adminClient.updateRevenue(LocalDate.now(), -updated.getTotalAmount(), -1);
        } catch (Exception e) {
            System.err.println("Failed to update revenue: " + e.getMessage());
        }
        
        // Send notification
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("userId", String.valueOf(updated.getUserId()));
            notification.put("message", "Refund processed for order #" + updated.getOrderId() + ". Amount: ₹" + updated.getTotalAmount());
            notification.put("type", "REFUND_PROCESSED");
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
        
        return convertToDTO(updated);
    }
    
    private BillDTO convertToDTO(Bill bill) {
        BillDTO dto = new BillDTO();
        dto.setId(bill.getId());
        dto.setOrderId(bill.getOrderId());
        dto.setUserId(bill.getUserId());
        dto.setAmount(bill.getAmount());
        dto.setTax(bill.getTax());
        dto.setDeliveryCharge(bill.getDeliveryCharge());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setPaymentStatus(bill.getPaymentStatus());
        dto.setPaymentMethod(bill.getPaymentMethod());
        dto.setTransactionId(bill.getTransactionId());
        dto.setCreatedAt(bill.getCreatedAt());
        dto.setUpdatedAt(bill.getUpdatedAt());
        dto.setPaidAt(bill.getPaidAt());
        return dto;
    }
}