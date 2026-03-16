package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.menu.RecipeRequestDTO;
import com.example.food_buzzer_backend.dto.menu.RecipeResponseDTO;
import com.example.food_buzzer_backend.exception.ResourceNotFoundException;
import com.example.food_buzzer_backend.model.InventoryMaterial;
import com.example.food_buzzer_backend.model.Recipe;
import com.example.food_buzzer_backend.model.RecipeItem;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.repository.InventoryMaterialRepository;
import com.example.food_buzzer_backend.repository.RecipeItemRepository;
import com.example.food_buzzer_backend.repository.RecipeRepository;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.UserRepository;
import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeItemRepository recipeItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private InventoryMaterialRepository inventoryMaterialRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public RecipeResponseDTO createRecipe(Long restaurantId, Long userId, RecipeRequestDTO requestDTO) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsLiveTrue(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
                
        // Validate User and Access Level
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("User does not belong to this restaurant");
        }
        if (user.getAccessLevel() < AppConstants.ACCESS_LEVEL_MANAGER) {
            throw new IllegalArgumentException("User does not have permission to create recipes");
        }

        // 1. Create and save Recipe
        Recipe recipe = new Recipe();
        recipe.setRestaurant(restaurant);
        recipe.setName(requestDTO.getName());
        recipe.setDescription(requestDTO.getDescription());
        Recipe savedRecipe = recipeRepository.save(recipe);

        // 2. Create and save RecipeItems
        List<RecipeItem> items = requestDTO.getItems().stream().map(itemDTO -> {
            InventoryMaterial material = inventoryMaterialRepository
                    .findByIdAndRestaurantIdAndIsActiveTrueAndIsDeletedFalse(itemDTO.getRawMaterialId(), restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Raw material not found for ID: " + itemDTO.getRawMaterialId()));

            RecipeItem item = new RecipeItem();
            item.setRecipe(savedRecipe);
            item.setRawMaterial(material);
            item.setQuantity(itemDTO.getQuantity());
            return recipeItemRepository.save(item);
        }).collect(Collectors.toList());

        return mapToResponseDTO(savedRecipe, items);
    }

    public List<RecipeResponseDTO> getAllRecipes(Long restaurantId, Long userId) {
        if (!restaurantRepository.existsByIdAndIsLiveTrue(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        
        // Validate User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("User does not belong to this restaurant");
        }

        List<Recipe> recipes = recipeRepository.findByRestaurantIdAndIsDeletedFalse(restaurantId);

        return recipes.stream().map(recipe -> {
            List<RecipeItem> items = recipeItemRepository.findByRecipeId(recipe.getId());
            return mapToResponseDTO(recipe, items);
        }).collect(Collectors.toList());
    }

    public RecipeResponseDTO getRecipeById(Long restaurantId, Long userId, Long recipeId) {
        // Validate User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("User does not belong to this restaurant");
        }

        Recipe recipe = recipeRepository.findByIdAndRestaurantIdAndIsDeletedFalse(recipeId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        List<RecipeItem> items = recipeItemRepository.findByRecipeId(recipeId);
        return mapToResponseDTO(recipe, items);
    }

    // Helper mapping method
    private RecipeResponseDTO mapToResponseDTO(Recipe recipe, List<RecipeItem> items) {
        RecipeResponseDTO responseDTO = new RecipeResponseDTO();
        responseDTO.setId(recipe.getId());
        responseDTO.setName(recipe.getName());
        responseDTO.setDescription(recipe.getDescription());
        responseDTO.setCreatedAt(recipe.getCreatedAt());
        responseDTO.setUpdatedAt(recipe.getUpdatedAt());

        List<RecipeResponseDTO.RecipeItemResponseDTO> itemDTOs = items.stream().map(item -> {
            RecipeResponseDTO.RecipeItemResponseDTO itemDTO = new RecipeResponseDTO.RecipeItemResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setRawMaterialId(item.getRawMaterial().getId());
            itemDTO.setRawMaterialName(item.getRawMaterial().getName());
            itemDTO.setRawMaterialUnit(item.getRawMaterial().getUnit());
            itemDTO.setQuantity(item.getQuantity());
            return itemDTO;
        }).collect(Collectors.toList());

        responseDTO.setItems(itemDTOs);
        return responseDTO;
    }
}
