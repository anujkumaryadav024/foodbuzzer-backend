package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.InventoryMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryMaterialRepository extends JpaRepository<InventoryMaterial, Long> {

    Optional<InventoryMaterial> findBySkuAndRestaurantId(String sku, Long restaurantId);
    
    // Find active (non-deleted) item by SKU and restaurant
    Optional<InventoryMaterial> findBySkuAndRestaurantIdAndIsDeletedFalse(String sku, Long restaurantId);

    // Get all active (-deleted) inventory items for a restaurant
    List<InventoryMaterial> findByRestaurantIdAndIsDeletedFalse(Long restaurantId);

    // Search by exact name for a specific restaurant (non-deleted items only) - exact match, case-insensitive
    List<InventoryMaterial> findByNameIgnoreCaseAndRestaurantIdAndIsDeletedFalse(String name, Long restaurantId);

    // Get low stock items (current_stock < reorderLevel) for a restaurant
    @Query("SELECT i FROM InventoryMaterial i WHERE i.restaurant.id = :restaurantId AND i.currentStock < i.reorderLevel AND i.isDeleted = false")
    List<InventoryMaterial> findLowStockItems(@Param("restaurantId") Long restaurantId);

    // Find raw material for a recipe ensuring it is active and not deleted
    Optional<InventoryMaterial> findByIdAndRestaurantIdAndIsActiveTrueAndIsDeletedFalse(Long id, Long restaurantId);
}

