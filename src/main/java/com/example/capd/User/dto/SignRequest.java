package com.example.capd.User.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest{
    private Long id;
    private String userId;
    private String username;
    private String password;
    private String Email;
    private char gender;
    private String address;
    private String Tendency;
    private String Phone;
}
