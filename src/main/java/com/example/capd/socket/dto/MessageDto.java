package com.example.capd.socket.dto;

import com.example.capd.socket.domain.Message;
import com.example.capd.team.domain.Room;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String message;
    private String senderId;
    private Long roomId;

    public Message toEntity(Room room) {
        return Message.builder()
                .timeStamp(LocalDateTime.now())
                .room(room)
                .senderId(senderId)
                .message(message)
                .build();
    }

    public static MessageDto fromEntity(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message.getMessage());
        messageDto.setSenderId(message.getSenderId());
        messageDto.setRoomId(message.getRoom().getId());
        return messageDto;
    }

}
