package com.example.capd.User.dto;

import com.example.capd.User.domain.Profile;
import com.example.capd.User.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileRequestDto {

    private String userId;
    private String intro; //한줄 소개
    private List<String> stackList; //스택
    private List<CareerParam> careers;
    private int myTime; // 내가 가능한 투자 가능 시간
    private int desiredTime; // 상대에게 바라는 투자 가능 시간
    private int collaborationCount; // 상대 협업 경험 횟수

    @Builder
    public ProfileRequestDto(String userId,String intro, List<String> stackList, int myTime, int desiredTime, int collaborationCount){
        this.userId = userId;
        this.intro = intro;
        this.stackList = stackList;
        this.myTime = myTime;
        this.desiredTime = desiredTime;
        this.collaborationCount = collaborationCount;
    }

    public Profile toEntity(User user) {
        return Profile.builder().intro(intro).stackList(stackList).
                myTime(myTime).desiredTime(desiredTime).collaborationCount(collaborationCount)
                .user(user).build();
    }

    public List<CareerParam> getCareers() {
        return careers;
    }

    public void setCareers(List<CareerParam> careers) {
        this.careers = careers;
    }

}
