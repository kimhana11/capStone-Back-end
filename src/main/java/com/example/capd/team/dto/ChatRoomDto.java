package com.example.capd.team.dto;

import com.example.capd.team.domain.Room;
import com.example.capd.team.domain.Team;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ChatRoomDto {
    private Long teamId;
    private String userId;
    private String name;
    private String password;
    private Long roomId;

    public Room toEntity(Team team) {
        return Room.builder()
                .timeStamp(new Date())
                .password(password)
                .name(name)
                .team(team)
                .build();
    }
}
