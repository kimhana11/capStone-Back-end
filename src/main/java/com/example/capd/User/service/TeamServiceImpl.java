package com.example.capd.User.service;

import com.example.capd.User.domain.Team;
import com.example.capd.User.dto.TeamParam;
import com.example.capd.User.repository.TeamMemberRepository;
import com.example.capd.User.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;


    @Override
    public void createTeam(TeamParam teamParam) {

    }

    @Override
    public void finalizeTeam(Team team) {

    }

    @Override
    public TeamParam getMyTeam(Long teamId) {
        return null;
    }

    @Override
    public List<TeamParam> MyteamList(String userId) {
        return null;
    }

    @Override
    public List<TeamParam> contestTeamList(String contestId) {
        return null;
    }

    @Override
    public void updateTeam(TeamParam teamParam) {

    }

    @Override
    public void deleteTeam(Long teamId) {

    }
}
