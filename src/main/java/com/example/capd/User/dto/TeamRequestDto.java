package com.example.capd.User.dto;

import com.example.capd.User.domain.Contest;
import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.TeamMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TeamRequestDto {

    private Boolean status;
    private List<String> memberIds;
    private Long contestId;
    private String leaderId;


    public Team toEntity(Contest contest){
        return Team.builder()
                .status(status)
                .leaderId(leaderId)
                .contest(contest).build();
    }

}
