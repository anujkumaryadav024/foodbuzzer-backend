package com.example.food_buzzer_backend.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.dto.admin.AdminApprovalRequest;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalResponse;
import com.example.food_buzzer_backend.dto.admin.AdminDashboardResponse;
import com.example.food_buzzer_backend.dto.admin.AdminRestaurantByUserResponse;
import com.example.food_buzzer_backend.dto.admin.RestaurantDetailsResponse;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.RestaurantRepository;

@Service
public class AdminService {

    private final RestaurantRepository restaurantRepository;

    public AdminService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // Converts entity -> response DTO
    private RestaurantDetailsResponse toDetails(Restaurant r) {
        User owner = r.getOwner();
        Long ownerId = owner != null ? owner.getId() : null;
        String ownerName = owner != null ? owner.getFullName() : null;
        String ownerEmail = owner != null ? owner.getEmail() : null;

        return new RestaurantDetailsResponse(
                r.getId(),
                r.getName(),
                r.getSlug(),
                r.getAddress(),
                r.getCity(),
                r.getZipcode(),
                r.getPhone(),
                r.getApprovalStatus(),
                r.getApprovalNote(),
                ownerId,
                ownerName,
                ownerEmail
        );
    }

    public AdminDashboardResponse getRequestsByStatus(String status) {
        // Fetch restaurants grouped by status
        List<Restaurant> restaurants;
        if (status == null || status.equalsIgnoreCase("all")) {
            restaurants = restaurantRepository.findAll();
            status = "all";
        } else {
            restaurants = restaurantRepository.findByApprovalStatus(status.toLowerCase());
        }

        List<RestaurantDetailsResponse> responses = new ArrayList<>();

        for (Restaurant r : restaurants) {
            responses.add(toDetails(r));
        }

        long pendingCount = restaurantRepository.countByApprovalStatus("pending");
        long approvedCount = restaurantRepository.countByApprovalStatus("approved");
        long declinedCount = restaurantRepository.countByApprovalStatus("declined");

        return new AdminDashboardResponse(
                status.toUpperCase(),
                responses,
                responses.size(),
                pendingCount,
                approvedCount,
                declinedCount,
                "Requests fetched"
        );
    }

    public AdminRestaurantByUserResponse getRestaurantByOwnerUserId(Long ownerUserId) {
        if (ownerUserId == null) {
            return new AdminRestaurantByUserResponse(null, "ownerUserId is required", 422);
        }

        List<Restaurant> restaurants = restaurantRepository.findByOwner_Id(ownerUserId);
        if (restaurants.isEmpty()) {
            return new AdminRestaurantByUserResponse(null, "No restauarants found for user", 404);
        }

        List<RestaurantDetailsResponse> responses = new ArrayList<>();

        for (Restaurant r : restaurants) {
            responses.add(toDetails(r));
        }

        return new AdminRestaurantByUserResponse(responses, "Restaurants fetched", 200);
    }

    public AdminApprovalResponse updateApproval(Long adminUserId, AdminApprovalRequest request) {
        // adminUserId comes from header. Currently no auth, so just validate presence.
        if (adminUserId == null) {
            return new AdminApprovalResponse("Missing admin user id in header (X-User-Id)", 422);
        }
        if (request == null) {
            return new AdminApprovalResponse("Request body is required", 422);
        }
        if (request.getRestaurantId() == null) {
            return new AdminApprovalResponse("ownerUserId is required", 422);
        }
        if (request.getIsApproved() == null) {
            return new AdminApprovalResponse("isApproved is required", 422);
        }

        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(request.getRestaurantId());
        if (restaurantOpt.isEmpty()) {
            return new AdminApprovalResponse("Restaurant not found for given owner user id", 404);
        }

        Restaurant restaurant = restaurantOpt.get();

        // Set status based on boolean
        if (Boolean.TRUE.equals(request.getIsApproved())) {
            restaurant.setApprovalStatus("APPROVED");
        } else {
            restaurant.setApprovalStatus("DECLINED");
        }

        // Save notes if provided
        restaurant.setApprovalNote(request.getApprovalNotes());

        restaurantRepository.save(restaurant);

        return new AdminApprovalResponse("Approval updated", 200);
    }
}