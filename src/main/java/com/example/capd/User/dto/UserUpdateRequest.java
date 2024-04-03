package com.example.capd.User.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String userId;
    private String newEmail;
    private String newPassword;
    private String newUsername;
    private char newGender;
    private String newAddress;
    private String newTendency;
    private String newPhone;
}