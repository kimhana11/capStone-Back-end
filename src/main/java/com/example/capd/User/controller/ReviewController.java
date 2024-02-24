package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.dto.ReviewRequestDto;
import com.example.capd.User.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
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
