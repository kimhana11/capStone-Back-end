package com.example.capd.User.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.User.dto.ParticipationParam;
import com.example.capd.User.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "참여 신청",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //참여할게요 취소
    @DeleteMapping("/participation/{contestId}/{userId}")
    public ResponseEntity<CommonResponse> deletePart(@PathVariable Long contestId, @PathVariable String userId){
        participationService.deleteParticipation(contestId, userId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "참여 취소",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
