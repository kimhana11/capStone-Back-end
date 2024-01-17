package com.example.capd.User.dto;

import com.example.capd.User.domain.Team;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TeamParam {

    private Boolean status;
    private List<String> memberIds;
    private Long contestId;
    private Long teamId; //응답용 teamId


}
