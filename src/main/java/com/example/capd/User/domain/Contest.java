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

    //참여할게요 매핑
    @OneToMany(mappedBy = "contest")
    private List<Participation> participations = new ArrayList<>();

    //유저 매핑
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
