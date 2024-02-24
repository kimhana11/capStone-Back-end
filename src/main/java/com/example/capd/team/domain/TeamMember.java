package com.example.capd.team.domain;

import com.example.capd.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

//User와 Team간의 다대다 관계를 일대다 관계로 풀어내기 위한 엔티티
/*여러 사용자가 하나의 팀에 속할 수 있고, 한 사용자가 여러 팀에 속할 수 있기 때문*/
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TeamMember(User user, Team team) {
       this.user=user;
       this.team=team;
    }

    public static TeamMember fromUserAndTeam(User user, Team team) {
        return new TeamMember(user, team);
    }

}
