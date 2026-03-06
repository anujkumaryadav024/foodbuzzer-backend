package com.example.food_buzzer_backend.config;

public final class AppConstants {

    private AppConstants() {}

    public static final String ROLE_OWNER = "owner";
    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_CASHIER = "cashier";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_ADMIN = "admin";

    public static final int ACCESS_LEVEL_DEFAULT = 0;
    public static final int ACCESS_LEVEL_STAFF = 0;
    public static final int ACCESS_LEVEL_CASHIER = 1;
    public static final int ACCESS_LEVEL_MANAGER = 2;
    public static final int ACCESS_LEVEL_OWNER = 3;
    public static final int ACCESS_LEVEL_ADMIN = 4;

    public static final String APPROVAL_STATUS_PENDING = "pending";
    public static final String APPROVAL_STATUS_APPROVED = "approved";
    public static final String APPROVAL_STATUS_DECLINED = "declined";

    public static final boolean DEFAULT_USER_ACTIVE = true;
    public static final boolean DEFAULT_RESTAURANT_LIVE = false;
    public static final String EMPTY_STRING = "";

    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_USER_NOT_OWNER = "User is not an owner";
    public static final String ERROR_INSUFFICIENT_ACCESS = "Insufficient access level";
}
