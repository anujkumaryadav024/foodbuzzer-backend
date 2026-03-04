package com.example.food_buzzer_backend.service;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.dto.request.LoginRequest;
import com.example.food_buzzer_backend.dto.request.RegisterOwnerRequest;
import com.example.food_buzzer_backend.dto.response.LoginResponse;
import com.example.food_buzzer_backend.dto.response.RegisterOwnerResponse;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public AuthService(UserRepository userRepository, RestaurantRepository restaurantRepository){
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public LoginResponse login(LoginRequest request){

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()){
            return new LoginResponse(null,null,"Invalid credentials");
        }

        User user = userOptional.get();

        if(!user.getPassword().equals(request.getPassword())){
            return new LoginResponse(null,null,"Invalid credentials");
        }

        if(!user.getIsActive()){
            return new LoginResponse(null,null,"User inactive");
        }

        return new LoginResponse(user.getId(), user.getRole(), "Login successful");
    }

    public RegisterOwnerResponse registerOwner(RegisterOwnerRequest request){

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("OWNER");
        user.setIsActive(true);

        userRepository.save(user);

        return new RegisterOwnerResponse(user.getId(), user.getRole(), "Owner registered successful");
    }
}