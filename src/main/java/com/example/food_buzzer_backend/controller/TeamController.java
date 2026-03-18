package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.food_buzzer_backend.service.TeamService;
import com.example.food_buzzer_backend.dto.team.TeamAddRequestDTO;
import com.example.food_buzzer_backend.dto.team.TeamCreationResponse;
import com.example.food_buzzer_backend.dto.team.TeamDeleteDTO;
import com.example.food_buzzer_backend.dto.team.TeamUpdateDTO;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/team-add")
    public TeamCreationResponse addTeam(
            @RequestHeader("User_Id") Long userId,
            @RequestBody TeamAddRequestDTO dto) {

        return teamService.addTeamMember(userId, dto);
    }

    @GetMapping("/team-list")
    public List<User> listTeam(
            @RequestHeader("User_Id") Long userId) {

        return teamService.getTeamMembers(userId);
    }

    @PutMapping("/team-delete")
    public TeamCreationResponse deleteTeam(
            @RequestHeader("User_Id") Long userId,
            @RequestBody TeamDeleteDTO dto) {

        return teamService.deleteMember(userId, dto);
    }

    @PutMapping("/team-update")
    public TeamCreationResponse updateTeam(
            @RequestHeader("User_Id") Long userId,
            @RequestBody TeamUpdateDTO dto) {

        return teamService.updateMember(userId, dto);
    }
}
