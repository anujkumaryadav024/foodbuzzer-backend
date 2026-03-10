package com.example.food_buzzer_backend.service;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantRequest;
import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantResponse;
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

    public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest request, Long userId){

        User owner = userRepository.findById(userId)
                .orElse(null);

        if (owner == null) {
            return new CreateRestaurantResponse(null, AppConstants.ERROR_USER_NOT_FOUND);
        }

        if (owner.getRole() == null || !AppConstants.ROLE_OWNER.equalsIgnoreCase(owner.getRole())) {
            return new CreateRestaurantResponse(null, AppConstants.ERROR_USER_NOT_OWNER);
        }

        if (owner.getAccessLevel() == null || owner.getAccessLevel() < AppConstants.ACCESS_LEVEL_OWNER) {
            return new CreateRestaurantResponse(null, AppConstants.ERROR_INSUFFICIENT_ACCESS);
        }

        if (restaurantRepository.existsBySlug(request.getSlug())) {
            return new CreateRestaurantResponse(null, "Slug '" + request.getSlug() + "' is already taken, please choose another.");
        }

        Restaurant restaurant = new Restaurant();

        restaurant.setName(request.getName());
        restaurant.setSlug(request.getSlug());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setZipcode(request.getZipcode());
        restaurant.setPhone(request.getPhone());
        restaurant.setOwner(owner);
        restaurant.setApprovalStatus(AppConstants.APPROVAL_STATUS_PENDING);
        restaurant.setApprovalNote(AppConstants.EMPTY_STRING);
        restaurant.setIsLive(AppConstants.DEFAULT_RESTAURANT_LIVE);

        restaurantRepository.save(restaurant);

        owner.setRestaurant(restaurant);
        userRepository.save(owner);

        return new CreateRestaurantResponse(restaurant.getId(), AppConstants.MSG_RESTAURANT_SUBMITTED_FOR_APPROVAL);
    }
}