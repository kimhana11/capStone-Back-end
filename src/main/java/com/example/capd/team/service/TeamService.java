package com.example.capd.team.service;

import com.example.capd.team.dto.TeamRequestDto;
import com.example.capd.team.dto.TeamParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeamService {

    //팀 생성
    public void createTeam(TeamRequestDto teamRequestDto);

    //단일 조회 (특정 팀 조회)
    public TeamParam getTeam(Long teamId);

    //팀 전체 조회 (본인이 속한 팀 리스트)
    public List<TeamParam> MyteamList(String userId);

    //팀 전체 조회 (공모전 참가 팀 리스트)
    public List<TeamParam> contestTeamList(Long contestId);

    //팀 해산
    public void deleteTeam(Long teamId);

    //팀 status 변경
    public void updateTeamStatus(Long teamId, Boolean newStatus, String userId);
    //팀원 수정
    public void addMembersToTeam(Long teamId, List<String> memberIds, String userId);
}
