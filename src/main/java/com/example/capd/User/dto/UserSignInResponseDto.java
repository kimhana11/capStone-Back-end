package com.example.capd.User.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class UserSignInResponseDto {

    private Long id;
    private String username;
    private String token;
}
