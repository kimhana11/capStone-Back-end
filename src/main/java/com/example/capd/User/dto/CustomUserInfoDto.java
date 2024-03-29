package com.example.capd.User.dto;

import com.example.capd.User.domain.Authority;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto{

    private Long id;
    private String userId;
    private String username;
    private String password;
    private String Email;
    private char gender;
    private String address;
    private String Tendency;
    private String Phone;
    private List<Authority> roles;


}

