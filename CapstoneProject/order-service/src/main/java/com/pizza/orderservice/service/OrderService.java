package com.pizza.orderservice.service;

import com.pizza.orderservice.client.AdminClient; // <--- NEW IMPORT
import com.pizza.orderservice.client.MenuClient;
import com.pizza.orderservice.client.NotificationClient;
import com.pizza.orderservice.dto.*;
import com.pizza.orderservice.entity.Order;
import com.pizza.orderservice.entity.OrderItem;
import com.pizza.orderservice.exception.OrderNotFoundException;
import com.pizza.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate; // <--- NEW IMPORT
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MenuClient menuClient;
    private final NotificationClient notificationClient;
    private final AdminClient adminClient; // <--- INJECTED NEW CLIENT
    
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setSpecialInstructions(request.getSpecialInstructions());
        order.setStatus(Order.OrderStatus.PENDING);
        
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        
        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItemDTO menuItem = menuClient.getMenuItemById(itemRequest.getMenuItemId());
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setPrice(menuItem.getPrice());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSubtotal(menuItem.getPrice() * itemRequest.getQuantity());
            
            orderItems.add(orderItem);
            totalAmount += orderItem.getSubtotal();
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        Order saved = orderRepository.save(order);
        
        // Notification Logic
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("userId", String.valueOf(saved.getUserId()));
            notification.put("message", "Your order #" + saved.getId() + " has been placed successfully!");
            notification.put("type", "ORDER_PLACED");
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
        
        return convertToDTO(saved);
    }
    
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }
    
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        // Check if status actually changed to prevent duplicate revenue updates
        boolean isStatusChangingToDelivered = (status == Order.OrderStatus.DELIVERED && order.getStatus() != Order.OrderStatus.DELIVERED);

        order.setStatus(status);
        Order updated = orderRepository.save(order);
        
        // --- LOGIC: IF DELIVERED -> INCREASE REVENUE ---
        if (isStatusChangingToDelivered) {
            try {
                System.out.println("Order Delivered! Updating Revenue: " + updated.getTotalAmount());
                adminClient.updateRevenue(LocalDate.now(), updated.getTotalAmount(), 1);
            } catch (Exception e) {
                System.err.println("Failed to update revenue: " + e.getMessage());
            }
        }
        // -----------------------------------------------
        
        // Notification
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("userId", String.valueOf(updated.getUserId()));
            notification.put("message", "Your order #" + updated.getId() + " status updated to: " + status);
            notification.put("type", "ORDER_STATUS_UPDATE");
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
        
        return convertToDTO(updated);
    }
    
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel a delivered order");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
        
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("userId", String.valueOf(order.getUserId()));
            notification.put("message", "Your order #" + order.getId() + " has been cancelled");
            notification.put("type", "ORDER_CANCELLED");
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(itemDTOs);
        
        return dto;
    }
    
    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setMenuItemName(item.getMenuItemName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}