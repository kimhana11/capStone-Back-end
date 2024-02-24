package com.example.capd.User.repository;

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

    Optional<Profile> findByUserId(Long userId);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.careers c WHERE p.user.userId = :userId")
    Optional<Profile> findProfileAndCareersByUserId(@Param("userId") String userId);


    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN u.profile p " +
            "WHERE EXISTS " +
            "(SELECT 1 FROM p.stackList s WHERE s IN :stackList)")
    List<User> findUsersByStackList(@Param("stackList") List<String> stackList);
}

