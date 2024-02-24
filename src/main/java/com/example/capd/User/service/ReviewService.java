package com.example.capd.User.service;

import com.example.capd.User.dto.ReviewRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    //리뷰 CRUD
    /* 1. 리뷰 받는 user가 존재하는지 확인
       2. 팀원수와 리뷰 수가 맞는지 확인 (수량 미달 가능, 수량 초과 불과)
    */
    //리뷰 저장
    public void saveReview(ReviewRequestDto reviewRequestDto);
    //리뷰 리스트 조회
    public List<ReviewRequestDto> getAllReview(String userId);
    //리뷰 평점 계산
    public double rateAverage(String userId);

}
