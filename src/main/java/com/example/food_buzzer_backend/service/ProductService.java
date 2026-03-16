package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.menu.ProductRequestDTO;
import com.example.food_buzzer_backend.dto.menu.ProductResponseDTO;
import com.example.food_buzzer_backend.exception.ResourceNotFoundException;
import com.example.food_buzzer_backend.model.Product;
import com.example.food_buzzer_backend.model.ProductRecipe;
import com.example.food_buzzer_backend.model.Recipe;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.repository.ProductRecipeRepository;
import com.example.food_buzzer_backend.repository.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRecipeRepository productRecipeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ProductResponseDTO createProduct(Long restaurantId, Long userId, ProductRequestDTO requestDTO) {
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
            throw new IllegalArgumentException("User does not have permission to create products");
        }

        if (productRepository.existsBySkuAndRestaurantId(requestDTO.getSku(), restaurantId)) {
            throw new IllegalArgumentException("Product with SKU '" + requestDTO.getSku() + "' already exists");
        }

        // 1. Create and save Product
        Product product = new Product();
        product.setRestaurant(restaurant);
        product.setName(requestDTO.getName());
        product.setSku(requestDTO.getSku());
        product.setCategory(requestDTO.getCategory());
        product.setPrice(requestDTO.getPrice());
        product.setIsLive(requestDTO.getIsLive() != null ? requestDTO.getIsLive() : true);
        Product savedProduct = productRepository.save(product);

        // 2. Create and save ProductRecipes (Assign recipes to product)
        List<ProductRecipe> recipes;
        if (requestDTO.getRecipes() != null && !requestDTO.getRecipes().isEmpty()) {
            recipes = requestDTO.getRecipes().stream().map(recipeDTO -> {
                Recipe recipe = recipeRepository.findByIdAndRestaurantIdAndIsDeletedFalse(recipeDTO.getRecipeId(), restaurantId)
                        .orElseThrow(() -> new ResourceNotFoundException("Recipe not found for ID: " + recipeDTO.getRecipeId()));

                ProductRecipe pr = new ProductRecipe();
                pr.setProduct(savedProduct);
                pr.setRecipe(recipe);
                pr.setQuantity(recipeDTO.getQuantity());
                return productRecipeRepository.save(pr);
            }).collect(Collectors.toList());
        } else {
            recipes = List.of(); // Empty list if no recipes assigned yet
        }

        return mapToResponseDTO(savedProduct, recipes);
    }

    public List<ProductResponseDTO> getAllProducts(Long restaurantId, Long userId) {
        if (!restaurantRepository.existsByIdAndIsLiveTrue(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        
        // Validate User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("User does not belong to this restaurant");
        }

        List<Product> products = productRepository.findByRestaurantIdAndIsDeletedFalse(restaurantId);

        return products.stream().map(product -> {
            List<ProductRecipe> recipes = productRecipeRepository.findByProductId(product.getId());
            return mapToResponseDTO(product, recipes);
        }).collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long restaurantId, Long userId, Long productId) {
        // Validate User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("User does not belong to this restaurant");
        }

        Product product = productRepository.findByIdAndRestaurantIdAndIsDeletedFalse(productId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<ProductRecipe> recipes = productRecipeRepository.findByProductId(productId);
        return mapToResponseDTO(product, recipes);
    }

    // Helper mapping method
    private ProductResponseDTO mapToResponseDTO(Product product, List<ProductRecipe> recipes) {
        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setSku(product.getSku());
        responseDTO.setCategory(product.getCategory());
        responseDTO.setPrice(product.getPrice());
        responseDTO.setIsLive(product.getIsLive());
        responseDTO.setCreatedAt(product.getCreatedAt());
        responseDTO.setUpdatedAt(product.getUpdatedAt());

        List<ProductResponseDTO.ProductRecipeResponseDTO> recipeDTOs = recipes.stream().map(pr -> {
            ProductResponseDTO.ProductRecipeResponseDTO prDTO = new ProductResponseDTO.ProductRecipeResponseDTO();
            prDTO.setId(pr.getId());
            prDTO.setRecipeId(pr.getRecipe().getId());
            prDTO.setRecipeName(pr.getRecipe().getName());
            prDTO.setQuantity(pr.getQuantity());
            return prDTO;
        }).collect(Collectors.toList());

        responseDTO.setRecipes(recipeDTOs);
        return responseDTO;
    }
}
