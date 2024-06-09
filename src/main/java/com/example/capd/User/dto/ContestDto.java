package com.example.capd.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContestDto {
    private Long contestId;
    private String title;

    @Builder
    ContestDto(Long contestId, String title){
        this.contestId = contestId;
        this.title = title;
    }
}
