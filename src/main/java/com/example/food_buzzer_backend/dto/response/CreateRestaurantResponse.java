package com.example.food_buzzer_backend.dto.response;

public class CreateRestaurantResponse {

    private Long restaurantId;
    private String message;

    public CreateRestaurantResponse(Long restaurantId, String message){
        this.restaurantId = restaurantId;
        this.message = message;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}