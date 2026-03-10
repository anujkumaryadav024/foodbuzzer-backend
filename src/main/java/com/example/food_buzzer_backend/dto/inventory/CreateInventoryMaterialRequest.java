package com.example.food_buzzer_backend.dto.inventory;

import java.math.BigDecimal;

/**
 * DTO for creating/adding a new inventory material item
 * Parameters to pass in POST request:
 * - name: Name of the material (e.g., "Tomatoes")
 * - sku: Unique stock keeping unit (e.g., "VEG-TOM-001")
 * - category: Category of material (e.g., "VEGETABLE")
 * - unit: Unit of measurement (e.g., "kg")
 * - currentStock: Initial stock quantity (e.g., 25.5)
 * - reorderLevel: Minimum stock level before reorder (e.g., 10)
 * - costPerUnit: Cost per unit amount (e.g., 30)
 * - userId: ID of the user creating this item (passed via X-User-Id header)
 */
public class CreateInventoryMaterialRequest {

    private String name;
    private String sku;
    private String category;
    private String unit;
    private Double currentStock;
    private Double reorderLevel;
    private BigDecimal costPerUnit;

    public CreateInventoryMaterialRequest() {}

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for sku
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    // Getter and Setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Getter and Setter for unit
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Getter and Setter for currentStock
    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }

    // Getter and Setter for reorderLevel
    public Double getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    // Getter and Setter for costPerUnit
    public BigDecimal getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit;
    }
}
