package com.example.capd.User.repository;

import com.example.capd.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import com.example.capd.User.domain.Review;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   User findByUsername(String username);

   Optional<User> findByUserId(String userId);

   List<Review> findReceivedReviewsByUserId(String userId);

   Optional<User> findFirstByUserId(String userId);

   @Query("SELECT u FROM User u WHERE u.username = :username")
   Optional<User> findOneWithAuthoritiesByUsername(String username);

   @Query("SELECT u FROM User u JOIN Participation part ON u.id = part.user.id WHERE part.contest.id = :contestId")
   List<User> findUsersByContestParticipation(@Param("contestId") Long contestId);

   List<User> findAllByUserIdIn(List<String> membersId);
}
