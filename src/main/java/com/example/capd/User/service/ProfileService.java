package com.example.capd.User.service;

import com.example.capd.User.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfileService {
    //프로필 CRUD
    /* 1. career랑 stack도 같이 저장, 조회
       2. user가 존재하는지 확인
       3. 존재하는 프로필 있는지 확인
    */
    //프로필 저장, 후기가 없을시 평점을 3.5로 설정
    public void saveProfile(ProfileRequestDto profileRequestDto);

    //단일 조회 (본인 프로필, 추천 프로필 자세히 볼때)
    public ProfileResponseDto getMyProfile(String userId);

    //필요스택 필터링 일치 하는 프로필 리스트 (ai 적용x)
    public List<ProfileParticipationRes> stackRecommendUsers(Long contestId, String userId);

    //프로필 전체 조회 (ai 추천 프로필), 공모전 id값 받기
    public List<ProfileParticipationRes> aiRecommendUsers(Long contestId, String userId);

    //프로필 수정
    public void editProfile(ProfileRequestDto profileRequestDto);
    //프로필 삭제(user 매핑 때문에, user 삭제되어야 프로필 삭제됨, user 탈퇴시 사용)
    public void deleteProfile(String userId);

    public void aiStart(Long contestId, String userId);

}
