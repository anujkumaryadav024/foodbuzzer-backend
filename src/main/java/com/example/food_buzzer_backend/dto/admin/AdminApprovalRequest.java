package com.example.food_buzzer_backend.dto.admin;

public class AdminApprovalRequest {
    private Long restaurantId;
    private Boolean isApproved;
    private String approvalNotes;

    public AdminApprovalRequest() {}

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public String getApprovalNotes() { return approvalNotes; }
    public void setApprovalNotes(String approvalNotes) { this.approvalNotes = approvalNotes; }
}