package com.example.capd.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomResponseDto {
    private Long teamId;
    private String userId;
    private String name;
    private String password;
    private Long roomId;

}
