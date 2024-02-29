package com.example.capd.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
@Builder
public class UserSignInDto {

    private String username;
    private String password;
}
