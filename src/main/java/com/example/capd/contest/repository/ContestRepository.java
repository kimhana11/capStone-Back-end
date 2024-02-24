package com.example.capd.contest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.capd.contest.domain.Contest;
import java.util.List;
import java.util.Map;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest findByTeams_Id(Long teamId);

    @Query(value = "select * from Contest", nativeQuery = true)
    List<Map<String, Object>> fetchDataFromDatabase();

    Contest findByTitle(String title);

    @Query(value = "SELECT c.id FROM Contest c", nativeQuery = true)
    List<Long> findAllIds();
}
