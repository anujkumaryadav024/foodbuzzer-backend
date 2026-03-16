package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {
    List<RecipeItem> findByRecipeId(Long recipeId);
    void deleteByRecipeId(Long recipeId);
}
