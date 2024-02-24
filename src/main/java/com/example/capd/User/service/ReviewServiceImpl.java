package com.example.capd.User.service;

import com.example.capd.Exception.TeamNotConfirmedException;
import com.example.capd.team.domain.Team;
import com.example.capd.User.domain.*;
import com.example.capd.User.dto.ReviewRequestDto;
import com.example.capd.User.repository.ReviewRepository;
import com.example.capd.team.repository.TeamRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        User reviewer = userRepository.findByUserId(reviewRequestDto.getReviewerId())
                .orElseThrow(() -> new EntityNotFoundException("작성자 id가 존재하지 않습니다: " + reviewRequestDto.getReviewerId()));

        User reviewedUser = userRepository.findByUserId(reviewRequestDto.getReviewedUserId())
                .orElseThrow(() -> new EntityNotFoundException("리뷰 받는 사람 id가 존재하지 않습니다: " + reviewRequestDto.getReviewedUserId()));

        //팀 현황이 확정일 경우에만 리뷰 작성 가능
        if (Boolean.TRUE.equals(team.getStatus())) {
            Review review = reviewRequestDto.toEntity(reviewer, reviewedUser, team);
            reviewRepository.save(review);
        } else {
            throw new TeamNotConfirmedException();
        }
    }

    @Override
    public List<ReviewRequestDto> getAllReview(String reviewedUserId) {
        User user = userRepository.findByUserId(reviewedUserId)
                .orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다: " + reviewedUserId));

        List<Review> reviews = reviewRepository.findReceivedReviewsByUserId(user.getId());
        return reviews.stream()
                .map(review -> convertToReviewDTO(review))
                .collect(Collectors.toList());
    }

    @Override
    public double rateAverage(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("id가 존재하지 않습니다:: " + userId));

        List<Review> receivedReviews = user.getReceivedReviews();
        if (!receivedReviews.isEmpty()) {
            double totalRating = receivedReviews.stream().mapToDouble(Review::getRate).sum();
            double averageRating = totalRating / receivedReviews.size();

            return Math.round(averageRating * 10.0) / 10.0;
        } else {
            return 3.5;
        }
    }

    private ReviewRequestDto convertToReviewDTO(Review review) {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setContent(review.getContent());
        reviewRequestDto.setRate(review.getRate());
        reviewRequestDto.setReviewerId(review.getReviewer().getUserId());
        reviewRequestDto.setReviewedUserId(review.getReviewedUser().getUserId());
        reviewRequestDto.setTeamId(review.getTeam().getId());

        return reviewRequestDto;
    }

}
