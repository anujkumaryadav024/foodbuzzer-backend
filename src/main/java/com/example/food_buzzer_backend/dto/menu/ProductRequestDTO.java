package com.example.food_buzzer_backend.dto.menu;

import java.math.BigDecimal;
import java.util.List;

public class ProductRequestDTO {

    private String name;
    private String sku;
    private String category;
    private BigDecimal price;
    private Boolean isLive = true;
    
    // We will pass the IDs of the recipes this product is composed of
    private List<ProductRecipeRequestDTO> recipes;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public List<ProductRecipeRequestDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ProductRecipeRequestDTO> recipes) {
        this.recipes = recipes;
    }

    // Inner class mapping a product to a recipe with an specific quantity
    public static class ProductRecipeRequestDTO {
        private Long recipeId;
        private Double quantity;

        // Getters and Setters
        public Long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
