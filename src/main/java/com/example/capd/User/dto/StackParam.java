package com.example.capd.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StackParam {

    private String userId;
    private Long contestId;
    private List<String> stackList;
}
