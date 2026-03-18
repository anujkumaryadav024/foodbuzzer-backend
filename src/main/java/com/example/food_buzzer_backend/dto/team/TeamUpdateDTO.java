package com.example.food_buzzer_backend.dto.team;

public class TeamUpdateDTO {

    private Long id;
    private String newRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewRole() {
        return newRole;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }
}
