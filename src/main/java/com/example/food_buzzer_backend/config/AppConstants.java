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
    public static final boolean DEFAULT_INVENTORY_ACTIVE = true;
    public static final String EMPTY_STRING = "";

    // Inventory Material Categories
    public static final String CATEGORY_VEGETABLE = "VEGETABLE";
    public static final String CATEGORY_MEAT = "MEAT";
    public static final String CATEGORY_DAIRY = "DAIRY";
    public static final String CATEGORY_SPICES = "SPICES";
    public static final String CATEGORY_OIL = "OIL";
    public static final String CATEGORY_OTHER = "OTHER";

    // Inventory Units
    public static final String UNIT_KG = "kg";
    public static final String UNIT_GRAM = "g";
    public static final String UNIT_LITER = "liter";
    public static final String UNIT_ML = "ml";
    public static final String UNIT_PIECE = "piece";
    public static final String UNIT_DOZEN = "dozen";

    public static final String MSG_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String MSG_USER_INACTIVE = "User inactive";
    public static final String MSG_LOGIN_SUCCESSFUL = "Login successful";
    public static final String MSG_EMAIL_ALREADY_REGISTERED = "Email already registered";
    public static final String MSG_OWNER_REGISTERED_SUCCESSFUL = "Owner registered successful";
    public static final String MSG_RESTAURANT_SUBMITTED_FOR_APPROVAL = "Restaurant submitted for approval";
    public static final String MSG_USER_NOT_ASSIGNED_TO_RESTAURANT = "User not associated with any restaurant";

    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_USER_NOT_OWNER = "User is not an owner";
    public static final String ERROR_INSUFFICIENT_ACCESS = "Insufficient access level";
}
