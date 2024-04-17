package com.example.capd.User.dto;

import lombok.Data;

@Data
public class CreateAccessTokenRequest {
    private String refreshToken;
}
