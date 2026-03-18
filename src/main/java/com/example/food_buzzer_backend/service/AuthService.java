package com.example.food_buzzer_backend.service;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.auth.LoginRequest;
import com.example.food_buzzer_backend.dto.auth.LoginResponse;
import com.example.food_buzzer_backend.dto.auth.RegisterOwnerRequest;
import com.example.food_buzzer_backend.dto.auth.RegisterOwnerResponse;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request){

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()){
            return new LoginResponse(null, null, null, AppConstants.MSG_INVALID_CREDENTIALS);
        }

        User user = userOptional.get();

        if(!user.getPassword().equals(request.getPassword())){
            return new LoginResponse(null, null, null, AppConstants.MSG_INVALID_CREDENTIALS);
        }

        if(!user.getIsActive()){
            return new LoginResponse(null, null, null, AppConstants.MSG_USER_INACTIVE);
        }

        if(user.getRestaurant() == null){
            return new LoginResponse(user.getId(), user.getRole(), user.getAccessLevel(), AppConstants.MSG_USER_NOT_ASSIGNED_TO_RESTAURANT);
        }

        return new LoginResponse(user.getId(), user.getRole(), user.getAccessLevel(), AppConstants.MSG_LOGIN_SUCCESSFUL);
    }

    public RegisterOwnerResponse registerOwner(RegisterOwnerRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterOwnerResponse(null, null, null, AppConstants.MSG_EMAIL_ALREADY_REGISTERED);
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(AppConstants.ROLE_OWNER);
        user.setAccessLevel(AppConstants.ACCESS_LEVEL_OWNER);
        user.setIsActive(AppConstants.DEFAULT_USER_ACTIVE);

        userRepository.save(user);

        return new RegisterOwnerResponse(user.getId(), user.getRole(), user.getAccessLevel(), AppConstants.MSG_OWNER_REGISTERED_SUCCESSFUL);
    }
}