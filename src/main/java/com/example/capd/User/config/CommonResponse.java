package com.example.capd.User.config;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommonResponse<T> {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private Object data;
    }

