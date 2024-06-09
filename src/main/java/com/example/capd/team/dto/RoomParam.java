package com.example.capd.team.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RoomParam
{

    private Boolean status;
    private String userId;
    private List<String> memberIds;
    private Long contestId;
    private Long roomId;
}
