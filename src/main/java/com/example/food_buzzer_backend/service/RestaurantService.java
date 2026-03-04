package com.example.food_buzzer_backend.service;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.dto.request.CreateRestaurantRequest;
import com.example.food_buzzer_backend.dto.response.CreateRestaurantResponse;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {

        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public CreateRestaurantResponse createRestaurantResponse(CreateRestaurantRequest request){

        User owner = userRepository.findById(request.getOwnerUserId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Restaurant restaurant = new Restaurant();

        restaurant.setName(request.getName());
        restaurant.setSlug(request.getSlug());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setZipcode(request.getZipcode());
        restaurant.setPhone(request.getPhone());
        restaurant.setOwner(owner);

        restaurantRepository.save(restaurant);

        return new CreateRestaurantResponse(restaurant.getId(), "Restaurant submitted for approval");
    }
}