package com.example.capd.team.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.team.dto.TeamParam;
import com.example.capd.team.dto.TeamRequestDto;
import com.example.capd.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/team-update/status/{userId}")
    public ResponseEntity<CommonResponse> updateTeamStatus(@RequestBody TeamParam teamParam, @PathVariable String userId){
        teamService.updateTeamStatus(teamParam.getTeamId(),teamParam.getStatus(), userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }


    @PostMapping("/team-update/members/{userId}")
    public ResponseEntity<CommonResponse> updateTeamMember(@RequestBody TeamParam teamParam, @PathVariable String userId){
        teamService.addMembersToTeam(teamParam.getTeamId(),teamParam.getMemberIds(), userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
