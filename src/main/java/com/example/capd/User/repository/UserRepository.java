package com.example.capd.User.repository;

import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    List<Review> findReceivedReviewsByUserId(String userId);
}
