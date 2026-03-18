package com.example.food_buzzer_backend.dto.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning inventory material information
 * This response contains all inventory item details fetched from database
 */
public class InventoryMaterialResponse {

    private Long id;
    private Long restaurantId;
    private String name;
    private String sku;
    private String category;
    private String unit;
    private Double currentStock;
    private Double reorderLevel;
    private BigDecimal costPerUnit;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryMaterialResponse() {}

    /**
     * Constructor with all parameters for creating response object
     */
    public InventoryMaterialResponse(Long id, Long restaurantId, String name, String sku, 
                                     String category, String unit, Double currentStock, 
                                     Double reorderLevel, BigDecimal costPerUnit, 
                                     Boolean isActive, Boolean isDeleted, 
                                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.sku = sku;
        this.category = category;
        this.unit = unit;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.costPerUnit = costPerUnit;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for restaurantId
    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

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

    // Getter and Setter for isActive
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Getter and Setter for isDeleted
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Getter and Setter for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter and Setter for updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
