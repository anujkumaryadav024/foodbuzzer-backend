package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.dto.inventory.ApiInventoryResponse;
import com.example.food_buzzer_backend.dto.inventory.CreateInventoryMaterialRequest;
import com.example.food_buzzer_backend.dto.inventory.InventoryMaterialResponse;
import com.example.food_buzzer_backend.dto.inventory.UpdateInventoryStockRequest;
import com.example.food_buzzer_backend.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for inventory material endpoints
 * Handles all HTTP requests related to inventory management
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Endpoint 1: Show all inventory items with their information
     * URL: GET /api/inventory/all
     * Required Parameters:
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     * 
     * Returns JSON array of all inventory items for the restaurant
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllInventoryItems(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        List<InventoryMaterialResponse> items = inventoryService.getAllInventoryItems(userId);

        // Check if user validation failed
        if (items == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "user is not active or restraunt not exist"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "All inventory items fetched successfully", items));
    }

    /**
     * Endpoint 2: Add a new item to inventory
     * URL: POST /api/inventory/add
     * Required Parameters:
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     *   - Request Body (JSON): CreateInventoryMaterialRequest
     */
    @PostMapping("/add")
    public ResponseEntity<?> addInventoryItem(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody CreateInventoryMaterialRequest request) {

        // First, validate the user
        Long restaurantId = inventoryService.validateUser(userId);
        
        if (restaurantId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "user is not active or restraunt not exist"));
        }

        // User is valid, now try to add the item
        InventoryMaterialResponse response = inventoryService.addInventoryItem(request, userId);

        if (response == null) {
            // If response is null after user validation, it must be SKU duplicate issue
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "item with this sku is already present"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiInventoryResponse(true, "Inventory item added successfully", response));
    }

    /**
     * Endpoint 3: Update an inventory item record
     * URL: PUT /api/inventory/update-stock/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to update
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     *   - Request Body (JSON): UpdateInventoryStockRequest with fields to update
     */
    @PutMapping("/update-stock/{itemId}")
    public ResponseEntity<?> updateInventoryStock(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody UpdateInventoryStockRequest request) {

        InventoryMaterialResponse response = inventoryService.updateInventoryStock(itemId, userId, request);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "wrong user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory stock updated successfully", response));
    }

    /**
     * Endpoint 4: Delete/soft delete an inventory item
     * URL: DELETE /api/inventory/delete/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to delete
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     * 
     * Note: This is a soft delete - the record is not removed from database,
     * but the is_deleted column is set to true
     */
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteInventoryItem(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        InventoryMaterialResponse response = inventoryService.deleteInventoryItem(itemId, userId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "wrong user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory item deleted successfully", response));
    }

    /**
     * Endpoint 5: Search for inventory items by exact name
     * URL: GET /api/inventory/search
     * Required Parameters:
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     *   - name (query parameter): Exact name of the item to search for (case-insensitive)
     * 
     * Returns JSON array of matching items (exact name match only)
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestParam(name = "name", required = true) String name) {

        List<InventoryMaterialResponse> items = inventoryService.searchByName(userId, name);
        
        // Check if user validation failed
        if (items == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "user is not active or restraunt not exist"));
        }

        // Check if items were found with the given name
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "item not found"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Search results", items));
    }

    /**
     * Endpoint 6: Get items running on low stock
     * URL: GET /api/inventory/low-stock
     * Required Parameters:
     *   - X-User-Id (header): ID of the user (restaurant is derived from user)
     * 
     * Returns JSON array of items with current_stock < reorderLevel
     */
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockItems(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        List<InventoryMaterialResponse> items = inventoryService.getLowStockItems(userId);

        // Check if user validation failed
        if (items == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "user is not active or restraunt not exist"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Low stock items retrieved successfully", items));
    }
}
