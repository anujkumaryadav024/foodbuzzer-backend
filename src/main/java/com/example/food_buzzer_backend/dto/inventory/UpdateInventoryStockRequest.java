package com.example.food_buzzer_backend.dto.inventory;

import java.math.BigDecimal;

/**
 * DTO for updating an inventory item record
 * Parameters to pass in PUT request - all fields are optional and only provided fields will be updated:
 * - name: String - Name of the material
 * - sku: String - Unique SKU code
 * - category: String - Category (VEGETABLE, MEAT, DAIRY, SPICES, OIL, OTHER)
 * - unit: String - Unit of measurement (kg, g, liter, ml, piece, dozen)
 * - currentStock: Double - Current stock quantity
 * - reorderLevel: Double - Minimum stock before reorder
 * - costPerUnit: BigDecimal - Cost per unit
 * - isActive: Boolean - Whether the item is active
 *
 * Example:
 * {
 *   "name": "Tomatoes",
 *   "sku": "VEG-TOM-001",
 *   "category": "VEGETABLE",
 *   "unit": "kg",
 *   "currentStock": 25.5,
 *   "reorderLevel": 10,
 *   "costPerUnit": 30,
 *   "isActive": true
 * }
 */
public class UpdateInventoryStockRequest {

    private String name;
    private String sku;
    private String category;
    private String unit;
    private Double currentStock;
    private Double reorderLevel;
    private BigDecimal costPerUnit;
    private Boolean isActive;

    public UpdateInventoryStockRequest() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }

    public Double getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
