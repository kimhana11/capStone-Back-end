package com.example.capd.User.repository;

import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
