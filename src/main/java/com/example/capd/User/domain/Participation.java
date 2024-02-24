package com.example.capd.User.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.example.capd.contest.domain.Contest;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String additional;
    private String time;

    //원하는 스택
    @ElementCollection
    @JsonManagedReference
    private List<String> stackList;

    //유저 매핑
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //공모전 매핑
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

}
