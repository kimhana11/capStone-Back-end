package com.example.capd.User.service;

import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.TeamMember;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.dto.TeamParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeamService {

    //팀 생성
    public void createTeam(TeamParam teamParam);

    //팀 확정
    public void finalizeTeam(Team team);

    //단일 조회 (특정 팀 조회)
    public TeamParam getMyTeam(Long teamId);

    //팀 전체 조회 (본인이 속한 팀 리스트)
    public List<TeamParam> MyteamList(String userId);

    //팀 전체 조회 (공모전 참가 팀 리스트)
    public List<TeamParam> contestTeamList(String contestId);

    //팀원 수정(팀 확정 아닌경우에만)
    public void updateTeam(TeamParam teamParam);

    //팀 해산
    public void deleteTeam(Long teamId);
}
