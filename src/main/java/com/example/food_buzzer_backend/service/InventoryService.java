package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.inventory.CreateInventoryMaterialRequest;
import com.example.food_buzzer_backend.dto.inventory.InventoryMaterialResponse;
import com.example.food_buzzer_backend.dto.inventory.UpdateInventoryStockRequest;
import com.example.food_buzzer_backend.model.InventoryMaterial;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.InventoryMaterialRepository;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling inventory material business logic
 * Contains methods for CRUD operations on inventory items
 */
@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryMaterialRepository inventoryMaterialRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public InventoryService(InventoryMaterialRepository inventoryMaterialRepository, 
                           RestaurantRepository restaurantRepository,
                           UserRepository userRepository) {
        this.inventoryMaterialRepository = inventoryMaterialRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get all inventory items for a specific restaurant (excluding deleted items)
     * @param restaurantId - ID of the restaurant
     * @param userId - ID of the user (for validation/context)
     * @return List of InventoryMaterialResponse objects containing all inventory items, or null if restaurant/user not found
     */
    public List<InventoryMaterialResponse> getAllInventoryItems(Long restaurantId, Long userId) {
        logger.info("getAllInventoryItems called with restaurantId: {} and userId: {}", restaurantId, userId);
        
        // Validate that both restaurant and user exist
        boolean restaurantExists = restaurantRepository.existsById(restaurantId);
        boolean userExists = userRepository.existsById(userId);
        
        logger.info("Restaurant {} exists: {}, User {} exists: {}", restaurantId, restaurantExists, userId, userExists);
        
        if (!restaurantExists || !userExists) {
            logger.warn("Validation failed: Restaurant or user not found");
            return null; // Restaurant or user not found - return null
        }
        
        // Fetch all non-deleted inventory items from database
        List<InventoryMaterial> materials = inventoryMaterialRepository.findByRestaurantIdAndIsDeletedFalse(restaurantId);
        
        logger.info("Found {} inventory items for restaurant {}", materials.size(), restaurantId);
        for (InventoryMaterial material : materials) {
            logger.info("Item: id={}, name={}, isDeleted={}", material.getId(), material.getName(), material.getIsDeleted());
        }
        
        // Convert to response DTOs and return
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Add a new inventory item to the database
     * @param request - CreateInventoryMaterialRequest containing item details (name, sku, category, unit, currentStock, reorderLevel, costPerUnit)
     * @param restaurantId - ID of the restaurant
     * @param userId - ID of the user creating this item
     * @return InventoryMaterialResponse with created item details, or null if restaurant/user not found or SKU already exists
     */
    public InventoryMaterialResponse addInventoryItem(CreateInventoryMaterialRequest request, Long restaurantId, Long userId) {
        // Check if restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        
        if (restaurant == null) {
            return null; // Restaurant not found
        }

        // Check if user exists
        User user = userRepository.findById(userId).orElse(null);
        
        if (user == null) {
            return null; // User not found
        }

        // Check if SKU already exists (SKU must be unique)
        if (inventoryMaterialRepository.findBySku(request.getSku()).isPresent()) {
            return null; // SKU already exists
        }

        // Create new inventory material entity
        InventoryMaterial material = new InventoryMaterial();
        material.setRestaurant(restaurant);
        material.setUser(user);
        material.setName(request.getName());
        material.setSku(request.getSku());
        material.setCategory(request.getCategory());
        material.setUnit(request.getUnit());
        material.setCurrentStock(request.getCurrentStock());
        material.setReorderLevel(request.getReorderLevel());
        material.setCostPerUnit(request.getCostPerUnit());
        material.setIsDeleted(false); // By default, item is not deleted
        material.setIsActive(true); // By default, item is active

        // Save to database
        InventoryMaterial savedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(savedMaterial);
    }

    /**
     * Update an inventory item record with all provided fields
     * @param itemId - ID of the inventory item to update
     * @param restaurantId - ID of the restaurant (to verify ownership)
     * @param userId - ID of the user (to verify ownership)
     * @param request - UpdateInventoryStockRequest containing fields to update (partial updates supported)
     * @return InventoryMaterialResponse with updated item details, or null if item not found or restaurant/user doesn't match
     */
    public InventoryMaterialResponse updateInventoryStock(Long itemId, Long restaurantId, Long userId, UpdateInventoryStockRequest request) {
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            return null; // Item not found
        }

        // Verify that the item belongs to the specified restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            return null; // Restaurant ID doesn't match the item's restaurant
        }

        // Verify that the item belongs to the specified user
        if (!material.getUser().getId().equals(userId)) {
            return null; // User ID doesn't match the item's user
        }

        // Update fields only if they are provided (non-null)
        if (request.getName() != null) {
            material.setName(request.getName());
        }
        if (request.getSku() != null) {
            material.setSku(request.getSku());
        }
        if (request.getCategory() != null) {
            material.setCategory(request.getCategory());
        }
        if (request.getUnit() != null) {
            material.setUnit(request.getUnit());
        }
        if (request.getCurrentStock() != null) {
            material.setCurrentStock(request.getCurrentStock());
        }
        if (request.getReorderLevel() != null) {
            material.setReorderLevel(request.getReorderLevel());
        }
        if (request.getCostPerUnit() != null) {
            material.setCostPerUnit(request.getCostPerUnit());
        }
        if (request.getIsActive() != null) {
            material.setIsActive(request.getIsActive());
        }
        
        // updated_at timestamp is automatically updated by @PreUpdate annotation
        // Save updated item to database
        InventoryMaterial updatedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(updatedMaterial);
    }

    /**
     * Soft delete an inventory item (sets is_delete to true instead of removing from database)
     * @param itemId - ID of the inventory item to delete
     * @param restaurantId - ID of the restaurant (to verify ownership)
     * @param userId - ID of the user (to verify ownership)
     * @return InventoryMaterialResponse with deleted item details, or null if item not found or restaurant/user doesn't match
     */
    public InventoryMaterialResponse deleteInventoryItem(Long itemId, Long restaurantId, Long userId) {
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            return null; // Item not found
        }

        // Verify that the item belongs to the specified restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            return null; // Restaurant ID doesn't match the item's restaurant
        }

        // Verify that the item belongs to the specified user
        if (!material.getUser().getId().equals(userId)) {
            return null; // User ID doesn't match the item's user
        }

        // Mark item as deleted (soft delete - record stays in database)
        material.setIsDeleted(true);
        
        // Save updated item to database
        InventoryMaterial deletedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(deletedMaterial);
    }

    /**
     * Search for inventory items by exact name (case-insensitive) for a specific restaurant
     * @param restaurantId - ID of the restaurant
     * @param userId - ID of the user (for validation)
     * @param name - Exact name of the item to search for (case-insensitive, must match full name)
     * @return List of InventoryMaterialResponse objects matching the search criteria, or null if restaurant/user not found
     */
    public List<InventoryMaterialResponse> searchByName(Long restaurantId, Long userId, String name) {
        // Validate that both restaurant and user exist
        if (!restaurantRepository.existsById(restaurantId) || !userRepository.existsById(userId)) {
            return null; // Restaurant or user not found - return null
        }
        
        // Search for items with exact name match (case-insensitive), excluding deleted items
        List<InventoryMaterial> materials = inventoryMaterialRepository
                .findByNameIgnoreCaseAndRestaurantIdAndIsDeletedFalse(name, restaurantId);
        
        // Convert to response DTOs and return (may be empty if no exact match found)
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all inventory items that are running on low stock (current_stock < reorderLevel)
     * @param restaurantId - ID of the restaurant
     * @param userId - ID of the user (for validation)
     * @return List of InventoryMaterialResponse objects for items with low stock, or null if restaurant/user not found
     */
    public List<InventoryMaterialResponse> getLowStockItems(Long restaurantId, Long userId) {
        logger.info("getLowStockItems called with restaurantId: {} and userId: {}", restaurantId, userId);
        
        // Validate that both restaurant and user exist
        boolean restaurantExists = restaurantRepository.existsById(restaurantId);
        boolean userExists = userRepository.existsById(userId);
        
        logger.info("Restaurant {} exists: {}, User {} exists: {}", restaurantId, restaurantExists, userId, userExists);
        
        if (!restaurantExists || !userExists) {
            logger.warn("Validation failed: Restaurant or user not found");
            return null; // Restaurant or user not found - return null
        }
        
        // Fetch all items where current_stock < reorderLevel and is_deleted = false
        List<InventoryMaterial> materials = inventoryMaterialRepository.findLowStockItems(restaurantId);
        
        logger.info("Found {} low stock items for restaurant {}", materials.size(), restaurantId);
        for (InventoryMaterial material : materials) {
            logger.info("Low stock item: id={}, name={}, currentStock={}, reorderLevel={}", 
                    material.getId(), material.getName(), material.getCurrentStock(), material.getReorderLevel());
        }
        
        // Convert to response DTOs and return
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert InventoryMaterial entity to InventoryMaterialResponse DTO
     * @param material - InventoryMaterial entity from database
     * @return InventoryMaterialResponse DTO with all item information
     */
    private InventoryMaterialResponse mapToResponse(InventoryMaterial material) {
        return new InventoryMaterialResponse(
                material.getId(),
                material.getRestaurant().getId(),
                material.getUser().getId(),
                material.getName(),
                material.getSku(),
                material.getCategory(),
                material.getUnit(),
                material.getCurrentStock(),
                material.getReorderLevel(),
                material.getCostPerUnit(),
                material.getIsActive(),
                material.getIsDeleted(),
                material.getCreatedAt(),
                material.getUpdatedAt()
        );
    }
}
