package com.example.capd.User.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //팀확정 상태. 팀이 확정되고, 공모전 접수 기간이 끝난다면 리뷰 작성
    private Boolean status;

    //팀멤버 매핑
    @OneToMany
    @JoinColumn(name = "team_id") // JoinColumn을 사용하여 외래키를 직접 지정
    private List<TeamMember> members = new ArrayList<>();

    //리뷰랑 매핑
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> teamReviews = new ArrayList<>();
}
