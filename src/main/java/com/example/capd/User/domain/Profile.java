package com.example.capd.User.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String intro; //한줄 소개
    private double rate; //평점, 기본 평점 3.5

    private int myTime; // 내가 가능한 투자 가능 시간
    private int desiredTime; // 상대에게 바라는 투자 가능 시간
    private int collaborationCount; // 상대 협업 경험 횟수

    //기술 스택
    @ElementCollection
    @JsonManagedReference
    private List<String> stackList;

    //유저와 일대일 매핑
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    //경력 매핑
    @JsonManagedReference
    @OneToMany(mappedBy = "profile", orphanRemoval = true)
    private List<Career> careers  = new ArrayList<>();

    public void setRate(double rate){
        this.rate = rate;
    }

    @Builder
    public Profile(Long id, String intro, List<String> stackList, User user, List<Career> careers, double rate,
                   int myTime, int desiredTime, int collaborationCount ){
        this.id=id;
        this.intro=intro;
        this.stackList=stackList;
        this.user=user;
        this.careers=careers;
        this.rate=rate;
        this.myTime = myTime;
        this.desiredTime = desiredTime;
        this.collaborationCount = collaborationCount;
    }
}
