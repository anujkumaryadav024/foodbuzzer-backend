package com.example.food_buzzer_backend.controller.menu;

import com.example.food_buzzer_backend.dto.ApiResponse;
import com.example.food_buzzer_backend.dto.menu.ProductRequestDTO;
import com.example.food_buzzer_backend.dto.menu.ProductResponseDTO;
import com.example.food_buzzer_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(restaurantId, userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {
        List<ProductResponseDTO> products = productService.getAllProducts(restaurantId, userId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @PathVariable Long productId) {
        ProductResponseDTO product = productService.getProductById(restaurantId, userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));
    }
}
