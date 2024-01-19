package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.domain.Team;
import com.example.capd.User.dto.ReviewRequestDto;
import com.example.capd.User.dto.TeamParam;
import com.example.capd.User.dto.TeamRequestDto;
import com.example.capd.User.service.TeamService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/team-save")
    public ResponseEntity<CommonResponse> saveTeam(@RequestBody TeamRequestDto teamRequestDto){
        teamService.createTeam(teamRequestDto);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    @PostMapping("/team-update/status/{userId}")
    public ResponseEntity<CommonResponse> updateTeamStatus(@RequestBody TeamParam teamParam, @PathVariable String userId){
        teamService.updateTeamStatus(teamParam.getTeamId(),teamParam.getStatus(), userId);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }


    @PostMapping("/team-update/members/{userId}")
    public ResponseEntity<CommonResponse> updateTeamMember(@RequestBody TeamParam teamParam, @PathVariable String userId){
        teamService.addMembersToTeam(teamParam.getTeamId(),teamParam.getMemberIds(), userId);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }


    @GetMapping("/team-get/{teamId}")
    public TeamParam getTeam(@PathVariable Long teamId){
        return teamService.getTeam(teamId);
    }

    @GetMapping("/teams-user/{userId}")
    public List<TeamParam> getMyTeamList(@PathVariable String userId){
        return teamService.MyteamList(userId);
    }

    @GetMapping("/teams-contest/{contestId}")
    public List<TeamParam> getContestTeamList(@PathVariable Long contestId){
        return teamService.contestTeamList(contestId);
    }

    @DeleteMapping("/team-delete/{teamId}")
    public ResponseEntity<CommonResponse> deleteTeam(@PathVariable Long teamId){
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }
}
