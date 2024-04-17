package com.example.capd.contest.controller;

import com.example.capd.contest.domain.Contest;
import com.example.capd.contest.service.ContestInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class ContestInfoController {

    private final ContestInfoService contestInfoService;

    @GetMapping("/showData")    // 백엔드 실행시 프론트 만들수있음
    public String showData(Model model) {
        List<Map<String, Object>> data = contestInfoService.fetchDataFromDatabase();
        model.addAttribute("data", data);
        return "showDataPage";
    }

    @GetMapping("/contestData") // 주소창에 입력하면 contest 정보 다 보여줌
    public ResponseEntity<List<Contest>> contestData() {
        return ResponseEntity.ok(contestInfoService.getAllContests());
    }

    //공모전 id 리스트
    @GetMapping("/contest-list")    // 콘테스트의 ID를 보내줌?
    public ResponseEntity<List<Long>> contestIdList(){
        return ResponseEntity.ok(contestInfoService.getContestId());
    }
}
