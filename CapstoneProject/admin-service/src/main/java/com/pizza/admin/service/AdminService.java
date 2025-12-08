package com.pizza.admin.service;

import com.pizza.admin.dto.*;
import com.pizza.admin.entity.Admin;
import com.pizza.admin.entity.Revenue;
import com.pizza.admin.exception.AdminNotFoundException;
import com.pizza.admin.exception.InvalidCredentialsException;
import com.pizza.admin.repository.AdminRepository;
import com.pizza.admin.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final RevenueRepository revenueRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AdminLoginResponse login(AdminLoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));
        
        String token = jwtService.generateToken(admin);
        return new AdminLoginResponse(token, convertToDTO(admin));
    }
    
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        return convertToDTO(admin);
    }
    
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public RevenueDTO addRevenue(RevenueDTO revenueDTO) {
        Revenue revenue = new Revenue();
        revenue.setDate(revenueDTO.getDate());
        revenue.setAmount(revenueDTO.getAmount());
        revenue.setOrderCount(revenueDTO.getOrderCount());
        revenue.setDescription(revenueDTO.getDescription());
        
        Revenue saved = revenueRepository.save(revenue);
        return convertToRevenueDTO(saved);
    }
    
    // --- THIS IS THE CRITICAL FIX FOR UPDATING REVENUE ---
    @Transactional
    public RevenueDTO updateRevenue(LocalDate date, Double amount, Integer orderCount) {
        // 1. Find existing record for this date OR create a new one
        Revenue revenue = revenueRepository.findByDate(date)
                .orElseGet(() -> {
                    Revenue newRev = new Revenue();
                    newRev.setDate(date);
                    newRev.setAmount(0.0);
                    newRev.setOrderCount(0);
                    newRev.setDescription("Daily Revenue");
                    return newRev;
                });
        
        // 2. ADD to the existing amount (Don't just overwrite!)
        revenue.setAmount(revenue.getAmount() + amount);
        revenue.setOrderCount(revenue.getOrderCount() + orderCount);
        
        // 3. Save
        Revenue saved = revenueRepository.save(revenue);
        return convertToRevenueDTO(saved);
    }
    // -----------------------------------------------------
    
    public List<RevenueDTO> getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return revenueRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToRevenueDTO)
                .collect(Collectors.toList());
    }
    
    public Double getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        Double total = revenueRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return total != null ? total : 0.0;
    }
    
    public Integer getTotalOrders(LocalDate startDate, LocalDate endDate) {
        Integer total = revenueRepository.getTotalOrdersBetweenDates(startDate, endDate);
        return total != null ? total : 0;
    }
    
    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setEmail(admin.getEmail());
        dto.setFullName(admin.getFullName());
        dto.setRole(admin.getRole());
        dto.setActive(admin.getActive());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        return dto;
    }
    
    private RevenueDTO convertToRevenueDTO(Revenue revenue) {
        RevenueDTO dto = new RevenueDTO();
        dto.setId(revenue.getId());
        dto.setDate(revenue.getDate());
        dto.setAmount(revenue.getAmount());
        dto.setOrderCount(revenue.getOrderCount());
        dto.setDescription(revenue.getDescription());
        return dto;
    }
}