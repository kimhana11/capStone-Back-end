package com.example.capd.User.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String intro; //한줄 소개
    private double rate; //평점, 기본 평점 3.5

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
    @OneToMany(mappedBy = "profile",  orphanRemoval = true)
    private List<Career> Careers = new ArrayList<>();

    public void setRate(double rate){
        this.rate = rate;
    }

}
