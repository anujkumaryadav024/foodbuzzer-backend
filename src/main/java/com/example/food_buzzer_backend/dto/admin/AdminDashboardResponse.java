package com.example.food_buzzer_backend.dto.admin;

import java.util.List;

public class AdminDashboardResponse {
    private String status;
    private List<RestaurantDetailsResponse> requests;
    private int requestsCount;

    private long pendingRequestsCount;
    private long approvedRequestsCount;
    private long declinedRequestsCount;
    private String message;
    private int statusCode;


    public AdminDashboardResponse() {}

    public AdminDashboardResponse(
            String status,
            List<RestaurantDetailsResponse> requests,
            int requestsCount,
            long pendingRequestsCount,
            long approvedRequestsCount,
            long declinedRequestsCount,
            String message
    ) {
        this.status = status;
        this.requests = requests;
        this.requestsCount = requestsCount;
        this.pendingRequestsCount = pendingRequestsCount;
        this.approvedRequestsCount = approvedRequestsCount;
        this.declinedRequestsCount = declinedRequestsCount;
        this.message = message;
    }

    public String getStatus() { return status; }
    public List<RestaurantDetailsResponse> getRequests() { return requests; }
    public int getRequestsCount() {return requestsCount; }
    public long getPendingRequestsCount() { return pendingRequestsCount; }
    public long getApprovedRequestsCount() { return approvedRequestsCount; }
    public long getDeclinedRequestsCount() { return declinedRequestsCount; }
    public String getMessage() { return message; }
}