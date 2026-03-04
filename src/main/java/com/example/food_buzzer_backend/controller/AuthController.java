package com.example.food_buzzer_backend.controller;


import org.springframework.web.bind.annotation.*;

import com.example.food_buzzer_backend.dto.request.LoginRequest;
import com.example.food_buzzer_backend.dto.request.RegisterOwnerRequest;
import com.example.food_buzzer_backend.dto.response.LoginResponse;
import com.example.food_buzzer_backend.dto.response.RegisterOwnerResponse;
import com.example.food_buzzer_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    
    @PostMapping("/register-owner")
    public RegisterOwnerResponse registerOwner(@RequestBody RegisterOwnerRequest request){

        return authService.registerOwner(request);
    }
}