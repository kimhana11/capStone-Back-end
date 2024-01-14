package com.example.capd.User.repository;

import com.example.capd.User.domain.Career;
import com.example.capd.User.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserId(Long userId);

    List<Profile> findProfilesByStackList(List<String> stackList);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.careers c WHERE p.user.userId = :userId")
    Optional<Profile> findProfileAndCareersByUserId(@Param("userId") String userId);
}
