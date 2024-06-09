package com.example.capd.User.dto;

import com.example.capd.User.domain.Career;
import com.example.capd.User.domain.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CareerParam {

    private Long id;
    private String title; //공모명
    private String stack; //프로젝트에서 사용한 스택
    private int period; //프로젝트 기간
    private String gitHub;

    @Builder
    public CareerParam(String title, String stack, int period, String gitHub){
        this.title = title;
        this.stack = stack;
        this.period = period;
        this.gitHub = gitHub;
    }

    public Career toEntity(Profile profile){
        return Career.builder().title(title).stack(stack).period(period).gitHub(gitHub).profile(profile).build();
    }
}
