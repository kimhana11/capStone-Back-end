package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.domain.Review;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.dto.ReviewRequestDto;
import com.example.capd.User.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //후기 저장
    @PostMapping("/user-review")
    public ResponseEntity<CommonResponse> saveReview(@RequestBody ReviewRequestDto reviewRequestDto){
        reviewService.saveReview(reviewRequestDto);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    //후기 전체 조회
    @GetMapping("/user-review/{userId}")
    public List<ReviewRequestDto> myReviewList(@PathVariable String userId){
        return reviewService.getAllReview(userId);
    }

    //후기 평점 조회
    @GetMapping("/user-rate/{userId}")
    public ResponseEntity<Double> myReviewRate(@PathVariable String userId) {
        try {
            double averageRate = reviewService.rateAverage(userId);
            return ResponseEntity.ok(averageRate);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
