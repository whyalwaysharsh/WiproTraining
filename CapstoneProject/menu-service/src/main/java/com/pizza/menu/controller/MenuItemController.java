package com.pizza.menu.controller;

import com.pizza.menu.dto.MenuItemDTO;
import com.pizza.menu.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu") // <--- KEY FIX: Matches Gateway Route
@RequiredArgsConstructor
// @CrossOrigin(origins = "*") <--- REMOVED to prevent Double-CORS error
public class MenuItemController {
    
    private final MenuItemService menuItemService;
    
    // --- ADMIN ENDPOINTS ---
    // The Gateway will send: /menu/admin/menu/add
    // So we map: /admin/menu/add (relative to class @RequestMapping "/menu")
    
    @PostMapping("/admin/menu/add") 
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO created = menuItemService.createMenuItem(menuItemDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @PutMapping("/admin/menu/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable Long id, 
            @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO updated = menuItemService.updateMenuItem(id, menuItemDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/admin/menu/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/admin/menu/{id}/availability")
    public ResponseEntity<MenuItemDTO> updateAvailability(
            @PathVariable Long id, 
            @RequestParam Boolean available) {
        MenuItemDTO updated = menuItemService.updateAvailability(id, available);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/admin/menu")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    // --- PUBLIC ENDPOINTS ---
    
    @GetMapping("/menu") // Resulting path: /menu/menu (If frontend calls /menu/menu)
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItemDTO> menuItems = menuItemService.getAvailableMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemDTO menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }
    
    @GetMapping("/menu/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> menuItems = menuItemService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }
}