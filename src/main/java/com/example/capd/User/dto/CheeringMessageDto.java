package com.example.capd.User.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheeringMessageDto {
    private String senderId;
    private String message;

    // senderId: message 형식의 문자열 반환
    public String toJsonString() {
        return senderId + ":" + message;
    }
}
