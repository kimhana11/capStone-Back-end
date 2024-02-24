package com.example.capd.contest.domain;

import com.example.capd.User.domain.Participation;
import com.example.capd.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity(name = "contest")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(length = 200)
    private String host;

    @Column(length = 200)
    private String targetParticipants;

    @Column(length = 40)
    private String receptionPeriod;//접수 기간 , (2023.02.11~2024.02.04 이런 형태라고 생각하고 진행)

    @Column(length = 40)
    private String decisionPeriod;

    @Column(length = 200)
    private String compatitionArea;

    @Column(length = 200)
    private String award;
    @Column(length = 300)
    private String homepage;

    @Column(length = 20)
    private String howToApply;

    @Column(length = 200)
    private String fee;

    @Column(length = 400)
    private String image;

    @Column(length = 2000)
    private String detailText;

    //참여할게요 매핑
    @OneToMany(mappedBy = "contest")
    private List<Participation> participations = new ArrayList<>();

    // 팀 매핑
    @OneToMany(mappedBy = "contest")
    private List<Team> teams = new ArrayList<>();
}