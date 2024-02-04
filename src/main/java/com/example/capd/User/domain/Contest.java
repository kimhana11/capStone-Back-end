package com.example.capd.User.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//테스트용
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String receptionPeriod; //접수 기간 , (2023.02.11~2024.02.04 이런 형태라고 생각하고 진행)

    //참여할게요 매핑
    @OneToMany(mappedBy = "contest")
    private List<Participation> participations = new ArrayList<>();

    //유저 매핑
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    // 팀 매핑
    @OneToMany(mappedBy = "contest")
    private List<Team> teams = new ArrayList<>();


}
