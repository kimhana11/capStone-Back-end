package com.example.capd.User.domain;

import com.example.capd.team.domain.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; //내용
    private double rate; //별점

    // 유저(리뷰 받는 사람)와 매핑
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "reviewed_user_id")
    @JsonIgnore
    private User reviewedUser;

    // 리뷰어(리뷰 쓴 사람)와 매핑
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

//    //팀이랑 매핑
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
