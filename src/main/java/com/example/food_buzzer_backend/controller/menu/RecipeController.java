package com.example.food_buzzer_backend.controller.menu;

import com.example.food_buzzer_backend.dto.ApiResponse;
import com.example.food_buzzer_backend.dto.menu.RecipeRequestDTO;
import com.example.food_buzzer_backend.dto.menu.RecipeResponseDTO;
import com.example.food_buzzer_backend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<ApiResponse> createRecipe(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody RecipeRequestDTO requestDTO) {
        RecipeResponseDTO responseDTO = recipeService.createRecipe(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recipe created successfully", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRecipes(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {
        List<RecipeResponseDTO> recipes = recipeService.getAllRecipes(userId);
        return ResponseEntity.ok(ApiResponse.success("Recipes retrieved successfully", recipes));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> getRecipeById(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @PathVariable Long recipeId){
        RecipeResponseDTO recipe = recipeService.getRecipeById(userId, recipeId);
        return ResponseEntity.ok(ApiResponse.success("Recipe retrieved successfully", recipe));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<ApiResponse> updateRecipe(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @PathVariable Long recipeId,
            @RequestBody RecipeRequestDTO requestDTO){
        RecipeResponseDTO responseDTO = recipeService.updateRecipe(userId, recipeId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Recipe updated successfully", responseDTO));
    }
}
