package com.example.capd.User.repository;

import com.example.capd.User.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.reviewedUser.id = :userId")
    List<Review> findReceivedReviewsByUserId(@Param("userId") Long userId);

}
