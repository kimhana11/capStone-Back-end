package com.example.capd.User.dto;

import com.example.capd.User.domain.Contest;
import com.example.capd.User.domain.Participation;
import com.example.capd.User.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ParticipationParam {

    private String userId;
    private Long contestId;
    private String additional;
    private String time;
    private List<String> stackList;

    public Participation toEntity(User user, Contest contest){
        return Participation.builder()
                .additional(additional)
                .time(time)
                .stackList(stackList)
                .contest(contest)
                .user(user).build();
    }

}
