package com.example.capd.User.dto;

import com.example.capd.User.domain.Authority;
import com.example.capd.User.domain.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;
    private String userId;
    private String username;
    private String Email;
    private char gender;
    private String address;
    private String Tendency;
    private String Phone;

    private List<Authority> roles = new ArrayList<>();
    private String token;

    public SignResponse(User user){
        this.id = user.getId();
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.Email = user.getEmail();
        this.gender = user.getGender();
        this.address = user.getAddress();
        this.Tendency = user.getTendency();
        this.Phone = user.getPhone();
        this.roles = user.getRoles();
    }

}
