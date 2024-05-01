package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.dto.ProfileParticipationRes;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "저장 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //프로필 삭제
    @DeleteMapping("/profile-delete/{userId}")
    public ResponseEntity<CommonResponse> deleteProfile(@PathVariable String userId){
        profileService.deleteProfile(userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //스택만으로 추천
    @GetMapping("profile-stack/{contestId}/{userId}")
    public List<ProfileParticipationRes> stackProfileList(@PathVariable Long contestId, @PathVariable String userId){
       return profileService.stackRecommendUsers(contestId, userId);
    }

    // 테스트
    @GetMapping("profile-ai/{contestId}/{userId}")
    public ResponseEntity<FileSystemResource> aiProfileList(@PathVariable Long contestId, @PathVariable Long userId){
        profileService.aiStart(contestId, userId);

        String filename = contestId + "_" + userId + ".json";

        FileSystemResource fileResource = new FileSystemResource(filename);
        if (fileResource.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fileResource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
