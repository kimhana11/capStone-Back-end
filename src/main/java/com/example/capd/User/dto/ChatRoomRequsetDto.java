package com.example.capd.User.dto;

import com.example.capd.User.domain.Room;
import com.example.capd.User.domain.Team;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ChatRoomRequsetDto {
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
