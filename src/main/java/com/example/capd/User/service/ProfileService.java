package com.example.capd.User.service;

import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.dto.StackParam;
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
    public List<ProfileResponseDto> stackProfileList(StackParam stackParam);

    //프로필 전체 조회 (ai 추천 프로필), 공모전 id값 받기
    public List<ProfileResponseDto> aiProfileList(String userId, Long contestId);

    //수정
    public void editProfile(ProfileRequestDto profileRequestDto);
    //삭제
    public void deleteProfile(String userId);


}
