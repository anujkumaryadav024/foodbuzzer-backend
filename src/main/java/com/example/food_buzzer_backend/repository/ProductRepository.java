package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByRestaurantIdAndIsDeletedFalse(Long restaurantId);
    Optional<Product> findByIdAndRestaurantIdAndIsDeletedFalse(Long id, Long restaurantId);
    boolean existsBySkuAndRestaurantId(String sku, Long restaurantId);
}
