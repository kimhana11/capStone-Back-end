package com.example.capd.User.service;

import com.example.capd.User.domain.*;
import com.example.capd.User.dto.TeamRequestDto;
import com.example.capd.User.dto.TeamParam;
import com.example.capd.User.repository.ContestRepository;
import com.example.capd.User.repository.TeamMemberRepository;
import com.example.capd.User.repository.TeamRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;


    @Override
    public void createTeam(TeamRequestDto teamRequestDto) {
        {
            List<String> memberIds = teamRequestDto.getMemberIds();
            Boolean status = teamRequestDto.getStatus();
            Long contestId = teamRequestDto.getContestId();

            Contest contest = contestRepository.findById(contestId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공모전 입니다: " + contestId));

            //팀 생성
            Team team = teamRequestDto.toEntity(contest);
            team = teamRepository.save(team);


            Team finalTeam = team;
            List<TeamMember> teamMembers = userRepository.findAllByUserIdIn(memberIds)
                    .stream()
                    .map(user -> TeamMember.fromUserAndTeam(user, finalTeam))
                    .collect(Collectors.toList());

            teamMemberRepository.saveAll(teamMembers);

        }
    }

    @Override
    public TeamParam getMyTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팀 아이디 입니다: " + teamId));

        return mapToDto(team);
    }

    @Override
    public List<TeamParam> MyteamList(String userId) {
        List<Team> teams = teamRepository.findAllByMembersUserUserId(userId);

        return teams.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamParam> contestTeamList(String contestId) {
        return null;
    }

    @Override
    public void updateTeam(TeamParam teamParam) {
        Long teamId = teamParam.getTeamId();
        Boolean status = teamParam.getStatus();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팀 아이디 입니다: " + teamId));

        // 팀 상태와 멤버 변경
        List<TeamMember> teamMembers = userRepository.findAllByUserIdIn(teamParam.getMemberIds())
                .stream()
                .map(user -> TeamMember.fromUserAndTeam(user, team))
                .collect(Collectors.toList());

        Team updatedTeam = Team.builder()
                .status(status)
                .members(teamMembers)
                .build();

        teamRepository.save(updatedTeam);
    }

    @Override
    public void deleteTeam(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    private TeamParam mapToDto(Team team) {
        TeamParam teamParam = new TeamParam();
        teamParam.setStatus(team.getStatus());
        teamParam.setTeamId(team.getId());
        teamParam.setContestId(team.getContest().getId());
        List<String> stringMemberIds = team.getMembers().stream()
                .map(teamMember -> teamMember.getUser().getUserId())
                .collect(Collectors.toList());
        teamParam.setMemberIds(stringMemberIds);

        return teamParam;
    }
}
