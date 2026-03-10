package com.example.food_buzzer_backend.dto.admin;

import java.util.List;

public class AdminRestaurantByUserResponse {
    private List<RestaurantDetailsResponse> restaurants;
    private String message;

    public AdminRestaurantByUserResponse() {}

    public AdminRestaurantByUserResponse(List<RestaurantDetailsResponse> restaurants, String message, int statusCode) {
        this.restaurants = restaurants;
        this.message = message;
    }

    public List<RestaurantDetailsResponse> getRestaurants() { return restaurants; }
    public String getMessage() { return message; }
}