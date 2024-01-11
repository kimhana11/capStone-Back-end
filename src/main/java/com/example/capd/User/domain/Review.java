package com.example.capd.User.domain;

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
    //유저와 매핑
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    //팀이랑 매핑
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
