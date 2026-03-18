package com.example.food_buzzer_backend.dto.admin;
public class RestaurantDetailsResponse {
    private Long restaurantId;
    private String name;
    private String slug;
    private String address;
    private String gst;
    private String zipcode;
    private String phone;
    private String approvalStatus;
    private String approvalNotes;

    private Long ownerUserId;
    private String ownerFullName;
    private String ownerEmail;

    public RestaurantDetailsResponse() {}

    public RestaurantDetailsResponse(
            Long restaurantId,
            String name,
            String slug,
            String address,
            String gst,
            String zipcode,
            String phone,
            String approvalStatus,
            String approvalNotes,
            Long ownerUserId,
            String ownerFullName,
            String ownerEmail
    ) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.slug = slug;
        this.address = address;
        this.gst = gst;
        this.zipcode = zipcode;
        this.phone = phone;
        this.approvalStatus = approvalStatus;
        this.approvalNotes = approvalNotes;
        this.ownerUserId = ownerUserId;
        this.ownerFullName = ownerFullName;
        this.ownerEmail = ownerEmail;
    }

    public Long getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getAddress() { return address; }
    public String getGST() { return gst; }
    public String getZipcode() { return zipcode; }
    public String getPhone() { return phone; }
    public String getApprovalStatus() { return approvalStatus; }
    public String getApprovalNotes() { return approvalNotes; }
    public Long getOwnerUserId() { return ownerUserId; }
    public String getOwnerFullName() { return ownerFullName; }
    public String getOwnerEmail() { return ownerEmail; }
}