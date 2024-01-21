package com.example.capd.User.repository;

import com.example.capd.User.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoomIdOrderByTimeStamp(Long roomId);
}
