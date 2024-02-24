package com.example.capd.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomPwDto {
    private Long roomId;
    private String password;
}
