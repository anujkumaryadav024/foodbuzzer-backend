package com.example.food_buzzer_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalRequest;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalResponse;
import com.example.food_buzzer_backend.dto.admin.AdminDashboardResponse;
import com.example.food_buzzer_backend.dto.admin.AdminRestaurantByUserResponse;
import com.example.food_buzzer_backend.dto.admin.RestaurantDetailsResponse;
import com.example.food_buzzer_backend.exception.ResourceNotFoundException;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class AdminService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public AdminService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    private RestaurantDetailsResponse toDetails(Restaurant restaurant) {
        User owner = restaurant.getOwner();
        Long ownerId = owner != null ? owner.getId() : null;
        String ownerName = owner != null ? owner.getFullName() : null;
        String ownerEmail = owner != null ? owner.getEmail() : null;

        return new RestaurantDetailsResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getSlug(),
                restaurant.getAddress(),
                restaurant.getGST(),
                restaurant.getZipcode(),
                restaurant.getPhone(),
                restaurant.getApprovalStatus(),
                restaurant.getApprovalNote(),
                ownerId,
                ownerName,
                ownerEmail
        );
    }

    private void validateAdminAccess(Long adminUserId) {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (adminUser.getAccessLevel() == null
                || adminUser.getAccessLevel() != AppConstants.ACCESS_LEVEL_ADMIN) {
            throw new SecurityException("Access denied. Admin access required");
        }
    }

    public AdminDashboardResponse getRequestsByStatus(Long adminUserId, String status) {
        validateAdminAccess(adminUserId);

        List<Restaurant> restaurants;
        String normalizedStatus;

        if (status == null || status.equalsIgnoreCase("all")) {
            restaurants = restaurantRepository.findAll();
            normalizedStatus = "all";
        } else {
            normalizedStatus = status.toLowerCase();
            restaurants = restaurantRepository.findByApprovalStatus(normalizedStatus);
        }

        List<RestaurantDetailsResponse> responses = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            responses.add(toDetails(restaurant));
        }

        long pendingCount = restaurantRepository.countByApprovalStatus(AppConstants.APPROVAL_STATUS_PENDING);
        long approvedCount = restaurantRepository.countByApprovalStatus(AppConstants.APPROVAL_STATUS_APPROVED);
        long declinedCount = restaurantRepository.countByApprovalStatus(AppConstants.APPROVAL_STATUS_DECLINED);

        return new AdminDashboardResponse(
                normalizedStatus.toUpperCase(),
                responses,
                responses.size(),
                pendingCount,
                approvedCount,
                declinedCount
        );
    }

    public AdminRestaurantByUserResponse getRestaurantByOwnerUserId(Long adminUserId, Long ownerUserId) {
        validateAdminAccess(adminUserId);

        if (ownerUserId == null) {
            throw new IllegalArgumentException("ownerUserId is required");
        }

        List<Restaurant> restaurants = restaurantRepository.findByOwner_Id(ownerUserId);
        if (restaurants.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants found for user");
        }

        List<RestaurantDetailsResponse> responses = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            responses.add(toDetails(restaurant));
        }

        return new AdminRestaurantByUserResponse(responses);
    }

    public AdminApprovalResponse updateApproval(Long adminUserId, AdminApprovalRequest request) {
        validateAdminAccess(adminUserId);

        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getRestaurantId() == null) {
            throw new IllegalArgumentException("restaurantId is required");
        }
        if (request.getIsApproved() == null) {
            throw new IllegalArgumentException("isApproved is required");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (Boolean.TRUE.equals(request.getIsApproved())) {
            restaurant.setApprovalStatus(AppConstants.APPROVAL_STATUS_APPROVED);
        } else {
            restaurant.setApprovalStatus(AppConstants.APPROVAL_STATUS_DECLINED);
        }

        restaurant.setApprovalNote(request.getApprovalNotes());
        restaurantRepository.save(restaurant);

        return new AdminApprovalResponse(
                restaurant.getId(),
                restaurant.getApprovalStatus(),
                restaurant.getApprovalNote()
        );
    }
}