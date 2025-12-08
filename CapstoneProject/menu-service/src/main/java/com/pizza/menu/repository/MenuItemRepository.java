package com.pizza.menu.repository;

import com.pizza.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategory(String category);
    List<MenuItem> findByAvailable(Boolean available);
    List<MenuItem> findByCategoryAndAvailable(String category, Boolean available);
}