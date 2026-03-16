package com.example.food_buzzer_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.food_buzzer_backend.dto.admin.AdminApprovalRequest;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalResponse;
import com.example.food_buzzer_backend.dto.admin.AdminDashboardResponse;
import com.example.food_buzzer_backend.dto.admin.AdminRestaurantByUserResponse;
import com.example.food_buzzer_backend.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // GET -> dashboard lists
    @GetMapping("/requests")
    public AdminDashboardResponse getRequestByStatus(@RequestParam(required = false) String status) {
        return adminService.getRequestsByStatus(status);
    }

    // GET -> restaurant details from owner user id
    @GetMapping("/restaurants/by-user/{userId}")
    public AdminRestaurantByUserResponse getRestaurantByUserId(@PathVariable("userId") Long userId) {
        return adminService.getRestaurantByOwnerUserId(userId);
    }

    // POST -> approve/decline
    // headers(user_id) => X-User-Id
    @PostMapping("/restaurants/approval")
    public AdminApprovalResponse updateApproval(
            @RequestHeader(value = "X-User-Id", required = false) Long adminUserId,
            @RequestBody AdminApprovalRequest request
    ) {
        return adminService.updateApproval(adminUserId, request);
    }
}