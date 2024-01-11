package com.example.capd.User.service;

import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.Team;
import com.example.capd.User.domain.TeamMember;
import com.example.capd.User.dto.ReviewRequestDto;
import com.example.capd.User.repository.ReviewRepository;
import com.example.capd.User.repository.TeamRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final TeamRepository teamRepository;

    @Override
    public void saveReview(ReviewRequestDto reviewRequestDto) {
        Team team = teamRepository.findById(reviewRequestDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않습니다: " + reviewRequestDto.getTeamId()));

        if (Boolean.TRUE.equals(team.getStatus())) {
            Review review = reviewRequestDto.toEntity();
            reviewRepository.save(review);
        } else {
            throw new IllegalStateException("저장된 리뷰가 없습니다/팀 현황 상태를 확인해주세요");
        }
    }


    @Override
    public List<Review> getAllReview(String userId) {
        return userRepository.findReceivedReviewsByUserId(userId);
    }

    @Override
    public double rateAverage(String userId) {
        List<Review> receivedReviews = userRepository.findReceivedReviewsByUserId(userId);
        if (!receivedReviews.isEmpty()) {
            double totalRating = receivedReviews.stream().mapToDouble(Review::getRate).sum();
            return totalRating / receivedReviews.size();
        } else {
            return 0.0;
        }
    }

}
