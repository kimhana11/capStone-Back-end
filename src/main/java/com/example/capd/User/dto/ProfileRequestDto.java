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

    @Builder
    public ProfileRequestDto(String userId,String intro, List<String> stackList){
        this.userId = userId;
        this.intro = intro;
        this.stackList = stackList;
    }

    public Profile toEntity(User user) {
        return Profile.builder().intro(intro).stackList(stackList).user(user).build();
    }

    public List<CareerParam> getCareers() {
        return careers;
    }

    public void setCareers(List<CareerParam> careers) {
        this.careers = careers;
    }

}
