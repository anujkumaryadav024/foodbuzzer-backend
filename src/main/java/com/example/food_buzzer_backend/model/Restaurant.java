package com.example.food_buzzer_backend.model;

import com.example.food_buzzer_backend.config.AppConstants;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    private String address;

    private String gst;

    private String zipcode;

    private String phone;

    private String approvalStatus = AppConstants.APPROVAL_STATUS_PENDING;

    private String approvalNote;

    private boolean isLive = AppConstants.DEFAULT_RESTAURANT_LIVE;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PostPersist
    public void generateSlug(){
        slug = this.name.toLowerCase().trim().replaceAll(" ","-")+"-"+this.id;
    }

    public Restaurant(){}

    public Long getId(){ return id; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getSlug(){ return slug; }

    public void setSlug(String slug){ this.slug = slug; }

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

    public String getGST(){ return gst; }

    public void setGST(String gst){ this.gst = gst; }

    public String getZipcode(){ return zipcode; }

    public void setZipcode(String zipcode){ this.zipcode = zipcode; }

    public String getPhone(){ return phone; }

    public void setPhone(String phone){ this.phone = phone; }

    public User getOwner(){ return owner; }

    public void setOwner(User owner){ this.owner = owner; }

    public String getApprovalStatus(){ return approvalStatus; }

    public void setApprovalStatus(String approvalStatus){ this.approvalStatus = approvalStatus; }

    public String getApprovalNote(){ return approvalNote; }

    public void setApprovalNote(String approvalNote){ this.approvalNote = approvalNote; }

    public boolean getIsLive(){ return isLive; }

    public void setIsLive(boolean isLive){ this.isLive = isLive; }

    public LocalDateTime getCreatedAt(){ return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }
}