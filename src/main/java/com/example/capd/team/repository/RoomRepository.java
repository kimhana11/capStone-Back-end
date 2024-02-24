package com.example.capd.team.repository;

import com.example.capd.team.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

      @Query("SELECT DISTINCT r FROM Room r JOIN r.team t WHERE t.id IN :teamIds")
      List<Room> findByTeamIdIn(@Param("teamIds") List<Long> teamIds);

      Room findByTeamId(Long teamId);
}
