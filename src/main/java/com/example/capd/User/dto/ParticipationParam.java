package com.example.capd.User.dto;

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

    private String additional;
    private String time;
    private List<String> stackList;

}
