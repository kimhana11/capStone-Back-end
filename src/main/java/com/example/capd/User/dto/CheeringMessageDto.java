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
}
