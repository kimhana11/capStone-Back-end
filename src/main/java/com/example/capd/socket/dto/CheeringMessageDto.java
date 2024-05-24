package com.example.capd.socket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheeringMessageDto {
    private String senderName;
    private String message;
}
