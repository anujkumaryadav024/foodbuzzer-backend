package com.example.food_buzzer_backend.dto.auth;
public class LoginResponse {

    private Long userId;
    private String role;
    private Integer accessLevel;
    private String message;

    public LoginResponse(){}

    public LoginResponse(Long userId, String role, Integer accessLevel, String message){
        this.userId = userId;
        this.role = role;
        this.accessLevel = accessLevel;
        this.message = message;
    }

    public Long getUserId(){ return userId; }

    public String getRole(){ return role; }

    public int getAccessLevel(){return accessLevel;}

    public String getMessage(){ return message; }

}