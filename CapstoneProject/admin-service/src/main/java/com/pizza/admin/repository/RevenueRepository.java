package com.pizza.admin.repository;

import com.pizza.admin.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    Optional<Revenue> findByDate(LocalDate date);
    List<Revenue> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(r.amount) FROM Revenue r WHERE r.date BETWEEN :startDate AND :endDate")
    Double getTotalRevenueBetweenDates(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(r.orderCount) FROM Revenue r WHERE r.date BETWEEN :startDate AND :endDate")
    Integer getTotalOrdersBetweenDates(LocalDate startDate, LocalDate endDate);
}