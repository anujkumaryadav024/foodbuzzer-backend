package com.example.food_buzzer_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.food_buzzer_backend.dto.request.CreateRestaurantRequest;
import com.example.food_buzzer_backend.dto.response.CreateRestaurantResponse;
import com.example.food_buzzer_backend.service.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public CreateRestaurantResponse createRestaurantResponse(@RequestBody CreateRestaurantRequest request){

        return restaurantService.createRestaurantResponse(request);
    }
}