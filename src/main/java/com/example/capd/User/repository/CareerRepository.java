package com.example.capd.User.repository;

import com.example.capd.User.domain.Career;
import com.example.capd.User.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
}
