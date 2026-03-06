package com.example.food_buzzer_backend.dto.auth;
public class LoginResponse {

    private Long userId;
    private String role;
    private String message;

    public LoginResponse(){}

    public LoginResponse(Long userId,String role,String message){
        this.userId = userId;
        this.role = role;
        this.message = message;
    }

    public Long getUserId(){ return userId; }

    public String getRole(){ return role; }

    public String getMessage(){ return message; }
}