package com.example.capd.User.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; //공모명
    private String stack; //프로젝트에서 사용한 스택
    private int period; //프로젝트 기간
    private String gitHub; //깃허브 주소 (null)

    //프로필과 매핑
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
