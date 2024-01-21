package com.example.capd.User.dto;

import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.Room;
import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ChatRoomDto {
    private Long teamId;
    private String userId;
    private String name;
    private String password;

    public Room toEntity(Team team) {
        return Room.builder()
                .timeStamp(new Date())
                .password(password)
                .name(name)
                .team(team)
                .build();
    }
}
