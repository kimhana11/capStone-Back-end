package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.dto.StackParam;
import com.example.capd.User.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    //프로필 저장
    @PostMapping("/user-profile")
    public ResponseEntity<CommonResponse> saveProfile(@RequestBody ProfileRequestDto profileRequestDto){
        profileService.saveProfile(profileRequestDto);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    //프로필 단일 조회
    @GetMapping("/user-profile/{userId}")
    public ProfileResponseDto myProfile(@PathVariable String userId){
        return profileService.getMyProfile(userId);
    }

    //프로필 수정
    @PostMapping("/profile-update")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody ProfileRequestDto profileRequestDto){
        profileService.editProfile(profileRequestDto);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    //프로필 삭제
    @DeleteMapping("/profile-delete/{userId}")
    public ResponseEntity<CommonResponse> deleteProfile(@PathVariable String userId){
        profileService.deleteProfile(userId);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    @PostMapping("users-stack")
    public List<ProfileResponseDto> stackProfileList(@RequestBody StackParam stackParam){
       return profileService.stackRecommendUsers(stackParam);
    }


}
