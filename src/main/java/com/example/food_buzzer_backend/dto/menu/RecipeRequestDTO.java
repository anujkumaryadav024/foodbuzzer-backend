package com.example.food_buzzer_backend.dto.menu;

import java.util.List;

public class RecipeRequestDTO {
    
    private String name;
    private String description;
    private List<RecipeItemRequestDTO> items;

    // Getters and Setters
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

    public List<RecipeItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<RecipeItemRequestDTO> items) {
        this.items = items;
    }

    public static class RecipeItemRequestDTO {
        private Long rawMaterialId;
        private Double quantity;

        // Getters and Setters
        public Long getRawMaterialId() {
            return rawMaterialId;
        }

        public void setRawMaterialId(Long rawMaterialId) {
            this.rawMaterialId = rawMaterialId;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
