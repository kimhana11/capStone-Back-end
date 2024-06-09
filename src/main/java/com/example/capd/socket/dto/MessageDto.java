package com.example.capd.socket.dto;

import com.example.capd.socket.domain.Message;
import com.example.capd.team.domain.Room;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String message;
    private String senderId;
    private String senderName;
    private Long roomId;

    public Message toEntity(Room room) {
        return Message.builder()
                .timeStamp(LocalDateTime.now())
                .room(room)
                .senderId(senderId)
                .message(message)
                .build();
    }

    @Builder
    MessageDto(String message, String senderId, String senderName, Long roomId){
        this.message = message;
        this.senderId = senderId;
        this.senderName = senderName;
        this.roomId = roomId;
    }

}
