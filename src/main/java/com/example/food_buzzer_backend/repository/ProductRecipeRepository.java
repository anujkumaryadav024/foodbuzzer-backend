package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.ProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRecipeRepository extends JpaRepository<ProductRecipe, Long> {
    List<ProductRecipe> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
