package com.example.capd.User.dto;

import com.example.capd.User.domain.User;
import com.example.capd.User.config.PasswordEncoderConfig;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {


    //private Long id;
    private String userId;
    private String name;
    private String password;
    private String Email;
    private char gender;
    private String address;
    private String Tendency;
    private String Phone;

    @Builder
    public User toEntity() {

            return User.builder()
                    .username(userId)
                    .password(password)
                    .Email(Email)
                    .gender(gender)
                    .address(address)
                    .Tendency(Tendency)
                    .Phone(Phone)
                    .build();
        }

}
