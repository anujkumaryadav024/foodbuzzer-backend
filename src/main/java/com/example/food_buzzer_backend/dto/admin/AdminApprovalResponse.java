package com.example.food_buzzer_backend.dto.admin;

public class AdminApprovalResponse {
    private String message;

    public AdminApprovalResponse() {}

    public AdminApprovalResponse(String message, int statusCode) {
        this.message = message;
    }

    public String getMessage() { return message; }
}