package com.example.capd.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotNull
    private String userId;


    @NotNull
    private String password;
}