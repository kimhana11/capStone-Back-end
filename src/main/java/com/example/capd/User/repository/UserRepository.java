package com.example.capd.User.repository;

import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    List<Review> findReceivedReviewsByUserId(String userId);

    @Query("SELECT u FROM User u JOIN Participation part ON u.id = part.user.id WHERE part.contest.id = :contestId")
    List<User> findUsersByContestParticipation(@Param("contestId") Long contestId);

    List<User> findAllByUserIdIn(List<String> membersId);

}
