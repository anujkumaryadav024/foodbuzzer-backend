package com.example.food_buzzer_backend.dto.restaurant;

import jakarta.validation.constraints.NotBlank;

public class CreateRestaurantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Zipcode is required")
    private String zipcode;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "GST is required")
    private String gst;

    public CreateRestaurantRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGST() {
        return gst;
    }

    public void setGST(String gst) {
        this.gst = gst;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}