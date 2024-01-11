package com.example.capd.User.repository;

import com.example.capd.User.domain.Career;
import com.example.capd.User.domain.Profile;
import com.example.capd.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(String userId);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH Career c ON p.id = c.profile.id WHERE p.user.userId = :userId")
    Optional<Profile> findProfileAndCareersByUserId(@Param("userId") String userId);
}
