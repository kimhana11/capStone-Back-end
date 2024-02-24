package com.example.capd.team.dto;

import com.example.capd.team.domain.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.example.capd.contest.domain.Contest;
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
