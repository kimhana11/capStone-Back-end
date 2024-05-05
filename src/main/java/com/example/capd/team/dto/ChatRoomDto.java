package com.example.capd.team.dto;

import com.example.capd.contest.domain.Contest;
import com.example.capd.team.domain.Room;
import lombok.*;

import java.awt.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ChatRoomDto {

    private Boolean status;
    private String name;
    private String leaderId;

    private List<String> memberIds;
    private Long contestId;
    private Long roomId;

    public Room toEntity(Contest contest) {
        return Room.builder()
                .timeStamp(new Date())
                .name(name)
                .leaderId(leaderId)
                .contest(contest)
                .build();
    }
}
