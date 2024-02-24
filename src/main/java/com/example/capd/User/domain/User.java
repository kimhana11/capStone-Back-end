package com.example.capd.User.domain;

import com.example.capd.team.domain.TeamMember;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String password;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String tendency;

    //프로필과 일대일 매핑
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

//    //팀멤버 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    //참여할게요 매핑
    @OneToMany(mappedBy = "user")
    private List<Participation> participations = new ArrayList<>();

    //리뷰 매핑
    @OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> receivedReviews = new ArrayList<>();


}
