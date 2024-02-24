package com.example.capd.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamParam {

    private Boolean status;
    private List<String> memberIds;
    private Long contestId;
    private Long teamId;
}
