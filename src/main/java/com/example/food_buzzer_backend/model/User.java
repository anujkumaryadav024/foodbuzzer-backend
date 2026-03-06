package com.example.food_buzzer_backend.model;

import com.example.food_buzzer_backend.config.AppConstants;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    private String role;

    private Integer accessLevel = AppConstants.ACCESS_LEVEL_DEFAULT;

    private Boolean isActive = AppConstants.DEFAULT_USER_ACTIVE;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public User(){}

    public Long getId(){ return id; }

    public String getFullName(){ return fullName; }

    public void setFullName(String fullName){ this.fullName = fullName; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }

    public void setPassword(String password){ this.password = password; }

    public String getPhone(){ return phone; }

    public void setPhone(String phone){ this.phone = phone; }

    public String getRole(){ return role; }

    public void setRole(String role){ this.role = role; }

    public Boolean getIsActive(){ return isActive; }

    public void setIsActive(Boolean isActive){ this.isActive = isActive; }

    public Integer getAccessLevel(){ return accessLevel; }

    public void setAccessLevel(Integer accessLevel){ this.accessLevel = accessLevel; }
}