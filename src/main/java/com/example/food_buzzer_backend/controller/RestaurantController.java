package com.example.food_buzzer_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantRequest;
import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantResponse;
import com.example.food_buzzer_backend.service.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public CreateRestaurantResponse createRestaurant(
            @RequestHeader(name = "X-User-Id" , required = true) Long userId,
            @RequestBody CreateRestaurantRequest request){
        return restaurantService.createRestaurant(request, userId);
    }
}