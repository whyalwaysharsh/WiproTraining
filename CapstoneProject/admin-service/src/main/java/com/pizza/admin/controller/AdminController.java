package com.pizza.admin.controller;

import com.pizza.admin.dto.*;
import com.pizza.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest request) {
        AdminLoginResponse response = adminService.login(request);
        return ResponseEntity.ok(response);
    }
    
    // --- THIS IS THE FIX ---
    // Changed "/{id}" to "/{id:[0-9]+}"
    // This forces Spring to ignore this method if the ID is not a number (like "login")
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Long id) {
        AdminDTO admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }
    
    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<AdminDTO> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
    
    @PostMapping("/revenue")
    public ResponseEntity<RevenueDTO> addRevenue(@RequestBody RevenueDTO revenueDTO) {
        RevenueDTO revenue = adminService.addRevenue(revenueDTO);
        return new ResponseEntity<>(revenue, HttpStatus.CREATED);
    }
    
    @PutMapping("/revenue")
    public ResponseEntity<RevenueDTO> updateRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Double amount,
            @RequestParam Integer orderCount) {
        RevenueDTO revenue = adminService.updateRevenue(date, amount, orderCount);
        return ResponseEntity.ok(revenue);
    }
    
    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueDTO>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RevenueDTO> revenues = adminService.getRevenueByDateRange(startDate, endDate);
        return ResponseEntity.ok(revenues);
    }
    
    @GetMapping("/revenue/total")
    public ResponseEntity<Map<String, Object>> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Double totalRevenue = adminService.getTotalRevenue(startDate, endDate);
        Integer totalOrders = adminService.getTotalOrders(startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalRevenue", totalRevenue);
        response.put("totalOrders", totalOrders);
        response.put("averageOrderValue", totalOrders > 0 ? totalRevenue / totalOrders : 0.0);
        
        return ResponseEntity.ok(response);
    }
}