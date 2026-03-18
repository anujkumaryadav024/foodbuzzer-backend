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
 * 
 * All operations are now user-centric:
 * - User ID is provided in the request
 * - User's restaurant is fetched from the users table
 * - User must be active and have a valid restaurant_id
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
     * Public helper method to validate user and extract restaurant ID
     * Can be used by controllers to validate user before calling service methods
     * @param userId - ID of the user
     * @return Restaurant ID if user is valid, active, and has a restaurant; null otherwise
     */
    public Long validateUser(Long userId) {
        return validateUserAndGetRestaurantId(userId);
    }

    /**
     * Helper method to validate user and extract restaurant ID
     * @param userId - ID of the user
     * @return Restaurant ID if user is valid, active, and has a restaurant; null otherwise
     */
    private Long validateUserAndGetRestaurantId(Long userId) {
        logger.info("Validating user ID: {}", userId);
        
        // Check if user exists
        User user = userRepository.findById(userId).orElse(null);
        
        if (user == null) {
            logger.warn("User {} not found", userId);
            return null;
        }
        
        // Check if user is active
        if (!user.getIsActive()) {
            logger.warn("User {} is not active", userId);
            return null;
        }
        
        // Check if user has a restaurant assigned
        if (user.getRestaurant() == null || user.getRestaurant().getId() == null) {
            logger.warn("User {} does not have a restaurant assigned", userId);
            return null;
        }
        
        logger.info("User {} validated successfully. Restaurant ID: {}", userId, user.getRestaurant().getId());
        return user.getRestaurant().getId();
    }

    /**
     * Get all inventory items for the restaurant associated with the user
     * @param userId - ID of the user
     * @return List of InventoryMaterialResponse objects or null if user is invalid
     */
    public List<InventoryMaterialResponse> getAllInventoryItems(Long userId) {
        logger.info("getAllInventoryItems called with userId: {}", userId);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Fetch all non-deleted inventory items for this restaurant
        List<InventoryMaterial> materials = inventoryMaterialRepository.findByRestaurantIdAndIsDeletedFalse(restaurantId);
        
        logger.info("Found {} inventory items for restaurant {}", materials.size(), restaurantId);
        
        // Convert to response DTOs and return
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Add a new inventory item to the restaurant associated with the user
     * @param request - CreateInventoryMaterialRequest containing item details
     * @param userId - ID of the user
     * @return InventoryMaterialResponse with created item details, or null if validation fails or SKU already exists
     */
    public InventoryMaterialResponse addInventoryItem(CreateInventoryMaterialRequest request, Long userId) {
        logger.info("addInventoryItem called with userId: {}", userId);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Fetch restaurant object
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        
        if (restaurant == null) {
            logger.error("Restaurant with ID {} not found", restaurantId);
            return null;
        }
        
        // Check if active (non-deleted) SKU already exists for this restaurant
        if (inventoryMaterialRepository.findBySkuAndRestaurantIdAndIsDeletedFalse(request.getSku(), restaurantId).isPresent()) {
            logger.warn("Item with SKU {} already exists for restaurant {}", request.getSku(), restaurantId);
            return null; // SKU already exists for this restaurant
        }
        
        // Create new inventory material entity
        InventoryMaterial material = new InventoryMaterial();
        material.setRestaurant(restaurant);
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
        
        logger.info("Inventory item created successfully: id={}, sku={}", savedMaterial.getId(), savedMaterial.getSku());
        
        // Return response DTO
        return mapToResponse(savedMaterial);
    }

    /**
     * Update an inventory item record
     * @param itemId - ID of the inventory item to update
     * @param userId - ID of the user (for validation and restaurant extraction)
     * @param request - UpdateInventoryStockRequest containing fields to update
     * @return InventoryMaterialResponse with updated item details, or null if validation fails
     */
    public InventoryMaterialResponse updateInventoryStock(Long itemId, Long userId, UpdateInventoryStockRequest request) {
        logger.info("updateInventoryStock called with itemId: {}, userId: {}", itemId, userId);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            logger.warn("Inventory item with ID {} not found", itemId);
            return null;
        }
        
        // Verify that the item belongs to the user's restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            logger.warn("Item {} does not belong to restaurant {}", itemId, restaurantId);
            return null;
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
        
        logger.info("Inventory item updated successfully: id={}", updatedMaterial.getId());
        
        // Return response DTO
        return mapToResponse(updatedMaterial);
    }

    /**
     * Soft delete an inventory item
     * @param itemId - ID of the inventory item to delete
     * @param userId - ID of the user (for validation and restaurant extraction)
     * @return InventoryMaterialResponse with deleted item details, or null if validation fails
     */
    public InventoryMaterialResponse deleteInventoryItem(Long itemId, Long userId) {
        logger.info("deleteInventoryItem called with itemId: {}, userId: {}", itemId, userId);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            logger.warn("Inventory item with ID {} not found", itemId);
            return null;
        }
        
        // Verify that the item belongs to the user's restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            logger.warn("Item {} does not belong to restaurant {}", itemId, restaurantId);
            return null;
        }
        
        // Mark item as deleted (soft delete - record stays in database) and inactive
        material.setIsDeleted(true);
        material.setIsActive(false);
        
        // Save updated item to database
        InventoryMaterial deletedMaterial = inventoryMaterialRepository.save(material);
        
        logger.info("Inventory item deleted successfully: id={}", deletedMaterial.getId());
        
        // Return response DTO
        return mapToResponse(deletedMaterial);
    }

    /**
     * Search for inventory items by exact name for the user's restaurant
     * @param userId - ID of the user (for validation and restaurant extraction)
     * @param name - Exact name of the item to search for (case-insensitive)
     * @return List of InventoryMaterialResponse objects or null if user is invalid
     */
    public List<InventoryMaterialResponse> searchByName(Long userId, String name) {
        logger.info("searchByName called with userId: {}, name: {}", userId, name);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Search for items with exact name match (case-insensitive), excluding deleted items
        List<InventoryMaterial> materials = inventoryMaterialRepository
                .findByNameIgnoreCaseAndRestaurantIdAndIsDeletedFalse(name, restaurantId);
        
        logger.info("Found {} items matching name '{}' for restaurant {}", materials.size(), name, restaurantId);
        
        // Convert to response DTOs and return (may be empty if no exact match found)
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all inventory items that are running on low stock for the user's restaurant
     * @param userId - ID of the user (for validation and restaurant extraction)
     * @return List of InventoryMaterialResponse objects for low stock items, or null if user is invalid
     */
    public List<InventoryMaterialResponse> getLowStockItems(Long userId) {
        logger.info("getLowStockItems called with userId: {}", userId);
        
        // Validate user and get restaurant ID
        Long restaurantId = validateUserAndGetRestaurantId(userId);
        
        if (restaurantId == null) {
            logger.warn("User validation failed for userId: {}", userId);
            return null;
        }
        
        // Fetch all items where current_stock < reorderLevel and is_deleted = false
        List<InventoryMaterial> materials = inventoryMaterialRepository.findLowStockItems(restaurantId);
        
        logger.info("Found {} low stock items for restaurant {}", materials.size(), restaurantId);
        
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
