package com.example.capd.User.dto;

import com.example.capd.User.domain.Review;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ReviewRequestDto {

    private String content;
    private double rate;
    private Long reviewerId;
    private Long teamId;

    public Review toEntity() {
        return Review.builder().content(content).rate(rate).build();
    }

}
