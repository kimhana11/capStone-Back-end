package com.example.capd.User.repository;

import com.example.capd.User.domain.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("SELECT p FROM Participation p " +
            "JOIN p.user u " +
            "JOIN p.contest c " +
            "WHERE c.id = :contestId AND u.id = :userId")
    Participation findParticipationByContestIdAndUserId(
            @Param("contestId") Long contestId,
            @Param("userId") Long userId
    );

    @Query("SELECT p FROM Participation p " +
            "JOIN p.user u " +
            "WHERE u.id = :userId")
    List<Participation> findParticipationsByUserId(@Param("userId") Long userId);

}




