package com.example.capd.User.dto;

import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.User;
import com.example.capd.team.domain.Room;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@AllArgsConstructor
public class ReviewRequestDto {

    private String content;
    private double rate;
    private String reviewerId; //리뷰 쓰는
    private String reviewedUserId; //리뷰 받는
    private Long roomId;
    private Long contestId;

    public Review toEntity(User reviewerId,User reviewedUserId, Room room) {
        return Review.builder()
                .content(content)
                .rate(rate)
                .reviewer(reviewerId)
                .reviewedUser(reviewedUserId)
                .room(room).build();
    }

}
