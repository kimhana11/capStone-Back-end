package com.example.capd.User.dto;

import com.example.capd.User.domain.Authority;
import com.example.capd.User.domain.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private String userId;
    private String username;
    private String password;
    private String Email;
    private char gender;
    private String address;
    private String Tendency;
    private String Phone;



    // UserDTO 클래스에서의 toEntity() 메서드
    @Builder
    public User toEntity() {

        return User.builder()
                .userId(userId)
                .username(username)
                .password(password)
                .Email(Email)
                .gender(gender)
                .address(address)
                .Tendency(Tendency)
                .Phone(Phone)
                .build();
    }


}
