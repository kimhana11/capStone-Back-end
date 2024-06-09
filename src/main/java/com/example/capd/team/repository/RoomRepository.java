package com.example.capd.team.repository;

import com.example.capd.team.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


      List<Room> findAllByMembersUserUserId(String userId);

      List<Room> findByContestId(Long contestId);

      @Query("SELECT r FROM Room r JOIN r.members m WHERE m.user.id = :userId")
      List<Room> findByMembersUserId(Long userId) ;
}
