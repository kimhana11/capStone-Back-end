package com.example.capd.User.service;

import com.example.capd.User.domain.Team;
import com.example.capd.User.dto.TeamParam;

import java.util.List;

public class TeamServiceImpl implements TeamService{
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
