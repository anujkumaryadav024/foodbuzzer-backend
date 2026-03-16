package com.example.food_buzzer_backend.dto.menu;

import java.time.LocalDateTime;
import java.util.List;

public class RecipeResponseDTO {
    
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RecipeItemResponseDTO> items;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<RecipeItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<RecipeItemResponseDTO> items) {
        this.items = items;
    }

    public static class RecipeItemResponseDTO {
        private Long id;
        private Long rawMaterialId;
        private String rawMaterialName;
        private String rawMaterialUnit;
        private Double quantity;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getRawMaterialId() {
            return rawMaterialId;
        }

        public void setRawMaterialId(Long rawMaterialId) {
            this.rawMaterialId = rawMaterialId;
        }

        public String getRawMaterialName() {
            return rawMaterialName;
        }

        public void setRawMaterialName(String rawMaterialName) {
            this.rawMaterialName = rawMaterialName;
        }

        public String getRawMaterialUnit() {
            return rawMaterialUnit;
        }

        public void setRawMaterialUnit(String rawMaterialUnit) {
            this.rawMaterialUnit = rawMaterialUnit;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
