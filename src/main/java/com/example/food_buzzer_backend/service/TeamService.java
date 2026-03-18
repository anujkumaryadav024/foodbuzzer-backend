package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.team.*;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final UserRepository userRepository;

    public TeamService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public TeamCreationResponse addTeamMember(Long ownerId, TeamAddRequestDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole())) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            response.setMessage("Already Present");
            return response;
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setRestaurant(owner.getRestaurant());
        user.setRole(dto.getRole());

        if (AppConstants.ROLE_STAFF.equals(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_STAFF);

        else if (AppConstants.ROLE_CASHIER.equals(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_CASHIER);

        else if (AppConstants.ROLE_MANAGER.equals(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_MANAGER);

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("User Created");

        return response;
    }

    public List<User> getTeamMembers(Long ownerId) {

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || owner.getRestaurant() == null)
            return List.of();

        return userRepository.findByRestaurantId(owner.getRestaurant().getId());
    }

    public TeamCreationResponse deleteMember(Long ownerId, TeamDeleteDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole())) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        User user = userRepository.findById(dto.getId()).orElse(null);

        if (user == null) {
            response.setMessage("User Invalid");
            return response;
        }

        user.setRestaurant(null);
	user.setAccessLevel(null);
        user.setIsActive(false);
 	user.setRestaurant(null);

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("User Removed");

        return response;
    }

    public TeamCreationResponse updateMember(Long ownerId, TeamUpdateDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole())) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        User user = userRepository.findById(dto.getId()).orElse(null);

        if (user == null) {
            response.setMessage("User Invalid");
            return response;
        }

        user.setRole(dto.getNewRole());

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("Role Updated");

        return response;
    }
}
