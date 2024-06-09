package com.example.capd.team.domain;

import com.example.capd.User.domain.Review;
import com.example.capd.contest.domain.Contest;
import com.example.capd.socket.domain.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean status;
    private String leaderId;
    private String name;
    private Date timeStamp;

    //팀멤버 매핑
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TeamMember> members = new ArrayList<>();

    //리뷰랑 매핑
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> roomReviews = new ArrayList<>();

    //공모전 매핑
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    public void setStatus(Boolean status){
        this.status = status;
    }

    //가장 최신 메시지 반환
    public Message getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.stream()
                .max(Comparator.comparing(Message::getId))
                .orElse(null);
    }
}