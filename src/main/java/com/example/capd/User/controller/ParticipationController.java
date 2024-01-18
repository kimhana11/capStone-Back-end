package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.dto.ParticipationParam;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ParticipationController {

    private final ParticipationService participationService;

    //참여할게요
    @PostMapping("/participation")
    public ResponseEntity<CommonResponse> savePart(@RequestBody ParticipationParam participationParam){
        participationService.saveParticipation(participationParam);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }

    //참여할게요 취소
    @DeleteMapping("/participation/{contestId}/{userId}")
    public ResponseEntity<CommonResponse> deletePart(@PathVariable Long contestId, @PathVariable String userId){
        participationService.deleteParticipation(contestId, userId);
        return ResponseEntity.ok(new CommonResponse("SUCCESS",200));
    }
}
