package com.example.capd.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccessTokenResponse {
    private String accessToken;
}
