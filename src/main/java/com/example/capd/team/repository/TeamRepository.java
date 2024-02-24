package com.example.capd.team.repository;

import com.example.capd.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByMembersUserUserId(String userId);
    List<Team> findByContestId(Long contestId);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.user.id = :userId")
    List<Team> findByMembersUserId(Long userId);

}

