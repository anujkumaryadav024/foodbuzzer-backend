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
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user
     * 
     * Returns JSON array of all inventory items for the restaurant
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "All inventory items fetched successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "userId": 5,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 25.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *       "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-05T12:00:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllInventoryItems(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        List<InventoryMaterialResponse> items = inventoryService.getAllInventoryItems(restaurantId, userId);

        // Check if restaurant or user is valid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant or user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "All inventory items fetched successfully", items));
    }

    /**
     * Endpoint 2: Add a new item to inventory
     * URL: POST /api/inventory/add
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user creating this item
     *   - Request Body (JSON): CreateInventoryMaterialRequest with fields:
     *     * name: String - Name of the material (e.g., "Tomatoes")
     *     * sku: String - Unique SKU code (e.g., "VEG-TOM-001")
     *     * category: String - Category (e.g., "VEGETABLE")
     *     * unit: String - Unit of measurement (e.g., "kg")
     *     * currentStock: Double - Initial stock quantity (e.g., 25.5)
     *     * reorderLevel: Double - Minimum stock before reorder (e.g., 10)
     *     * costPerUnit: BigDecimal - Cost per unit (e.g., 30)
     * 
     * Example Request:
     * {
     *   "name": "Tomatoes",
     *   "sku": "VEG-TOM-001",
     *   "category": "VEGETABLE",
     *   "unit": "kg",
     *   "currentStock": 25.5,
     *   "reorderLevel": 10,
     *   "costPerUnit": 30
     * }
     * 
     * Example Response (Success):
     * {
     *   "success": true,
     *   "message": "Inventory item added successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "userId": 5,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 25.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": false,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-01T10:00:00"
     *   }
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<?> addInventoryItem(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody CreateInventoryMaterialRequest request) {

        InventoryMaterialResponse response = inventoryService.addInventoryItem(request, restaurantId, userId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Failed to add inventory item. SKU might already exist or restaurant/user not found."));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiInventoryResponse(true, "Inventory item added successfully", response));
    }

    /**
     * Endpoint 3: Update an inventory item record
     * URL: PUT /api/inventory/update-stock/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to update
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user
     *   - Request Body (JSON): UpdateInventoryStockRequest with fields to update
     *     * name: String - Name of the material (optional)
     *     * sku: String - Unique SKU code (optional)
     *     * category: String - Category (optional)
     *     * unit: String - Unit of measurement (optional)
     *     * currentStock: Double - Current stock quantity (optional)
     *     * reorderLevel: Double - Minimum stock before reorder (optional)
     *     * costPerUnit: Double - Cost per unit (optional)
     *     * isActive: Boolean - Whether the item is active (optional)
     * 
     * Note: Provide only the fields you want to update. The updated_at timestamp is automatically updated.
     *       All provided fields will update the entire record.
     * 
     * Example Request (update entire record):
     * {
     *   "name": "Fresh Tomatoes",
     *   "sku": "VEG-TOM-001",
     *   "category": "VEGETABLE",
     *   "unit": "kg",
     *   "currentStock": 30.0,
     *   "reorderLevel": 15,
     *   "costPerUnit": 35,
     *   "isActive": true
     * }
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Inventory stock updated successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "userId": 5,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 15.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": false,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-07T14:30:00"
     *   }
     * }
     */
    @PutMapping("/update-stock/{itemId}")
    public ResponseEntity<?> updateInventoryStock(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody UpdateInventoryStockRequest request) {

        InventoryMaterialResponse response = inventoryService.updateInventoryStock(itemId, restaurantId, userId, request);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant or user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory stock updated successfully", response));
    }

    /**
     * Endpoint 4: Delete/soft delete an inventory item
     * URL: DELETE /api/inventory/delete/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to delete
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user
     * 
     * Note: This is a soft delete - the record is not removed from database,
     * but the is_delete column is set to true
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Inventory item deleted successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "userId": 5,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 15.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": true,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-07T14:35:00"
     *   }
     * }
     */
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteInventoryItem(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        InventoryMaterialResponse response = inventoryService.deleteInventoryItem(itemId, restaurantId, userId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant or user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory item deleted successfully", response));
    }

    /**
     * Endpoint 5: Search for inventory items by exact name
     * URL: GET /api/inventory/search
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user
     *   - name (query parameter): Exact name of the item to search for (case-insensitive, must match full name)
     * 
     * Example URL: GET /api/inventory/search?name=Tomatoes
     * 
     * Returns JSON array of matching items (exact name match only)
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Search results",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "userId": 5,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 15.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *       "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-07T14:30:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestParam(name = "name", required = true) String name) {

        List<InventoryMaterialResponse> items = inventoryService.searchByName(restaurantId, userId, name);
        
        // Check if restaurant or user is valid - returns null if invalid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant or user id"));
        }

        // Check if items were found with the given name
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong name entered"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Search results", items));
    }

    /**
     * Endpoint 6: Get items running on low stock
     * URL: GET /api/inventory/low-stock
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - X-User-Id (header): ID of the user
     * 
     * Returns JSON array of items with current_stock < reorderLevel
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Low stock items retrieved successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "userId": 5,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 5.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *       "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-07T14:30:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockItems(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        List<InventoryMaterialResponse> items = inventoryService.getLowStockItems(restaurantId, userId);

        // Check if restaurant or user is valid - returns null if invalid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant or user id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Low stock items retrieved successfully", items));
    }
}
