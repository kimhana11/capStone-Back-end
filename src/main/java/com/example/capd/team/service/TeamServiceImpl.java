package com.example.capd.team.service;


import com.example.capd.Exception.*;
import com.example.capd.team.domain.Team;
import com.example.capd.team.domain.TeamMember;
import com.example.capd.User.domain.*;
import com.example.capd.team.dto.TeamRequestDto;
import com.example.capd.team.dto.TeamParam;
import com.example.capd.contest.repository.ContestRepository;
import com.example.capd.team.repository.TeamMemberRepository;
import com.example.capd.team.repository.TeamRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.example.capd.contest.domain.Contest;
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

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

            if(memberIds.size()>6){
                throw new MemberLimitExceededException();
            }
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
    public TeamParam getTeam(Long teamId) {
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
    public List<TeamParam> contestTeamList(Long contestId) {
        List<Team> teams = teamRepository.findByContestId(contestId);
        return teams.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTeamStatus(Long teamId, Boolean newStatus,String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팀입니다.: " + teamId));

        User modifyingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + userId));

        if (!modifyingUser.getUserId().equals(team.getLeaderId())) {
            throw new UnauthorizedTeamMemberModificationException();
        }
        if (team.getStatus() != null && team.getStatus()) {
            throw new TeamAlreadyConfirmedException();
        }

        team.setStatus(newStatus);
        teamRepository.save(team);
    }

    @Override
    public void addMembersToTeam(Long teamId, List<String> memberIds, String userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 팀입니다.: " + teamId));

        User modifyingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + userId));

        if (!modifyingUser.getUserId().equals(team.getLeaderId())) {
            throw new UnauthorizedTeamMemberModificationException();
        }
        if (team.getStatus() != null && team.getStatus()) {
            throw new TeamAlreadyConfirmedException();
        }

        List<TeamMember> teamMembers = new ArrayList<>();

        for (String memberId : memberIds) {
            User user = userRepository.findByUserId(memberId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + memberId));

            // 이미 저장되어 있는 멤버인지 확인
            boolean isMember = team.getMembers().stream()
                    .anyMatch(member -> member.getUser().equals(user));

            if (!isMember) {
                TeamMember teamMember = TeamMember.builder()
                        .user(user)
                        .team(team)
                        .build();
                teamMembers.add(teamMember);
            }
        }

        team.getMembers().addAll(teamMembers);
        teamRepository.save(team);
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





