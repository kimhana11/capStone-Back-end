package com.example.capd.User.controller;

import com.example.capd.User.dto.CheeringMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CheerController {
    //실시간 응원글 테스트
    private final List<CheeringMessageDto> cheersQueue = new ArrayList<>();

    @PostMapping("/cheering-message")
    public ResponseEntity<?> postCheer(@RequestBody CheeringMessageDto cheer) {
        if (cheer.getMessage() == null || cheer.getMessage().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        // 최대 20개 유지
        if (cheersQueue.size() >= 20) {
            cheersQueue.remove(0); // 가장 처음에 저장된 메시지 삭제
        }

      //  cheer.setTimeStamp(new Date());
        cheersQueue.add(cheer);
        System.out.print(cheersQueue);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/cheering-message")
    public ResponseEntity<List<CheeringMessageDto>> getCheers() {
        return ResponseEntity.ok(cheersQueue);
    }
}