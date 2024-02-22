package com.example.capd.User.repository;

import com.example.capd.User.domain.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest findByTeams_Id(Long teamId);

    @Query("SELECT c.id FROM Contest c")
    List<Long> findAllIds();
}
