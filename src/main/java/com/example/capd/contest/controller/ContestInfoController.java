package com.example.capd.contest.controller;

import com.example.capd.contest.domain.Contest;
import com.example.capd.contest.service.ContestInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/contestData")
    public List<Contest> contestData() {
        return contestInfoService.getAllContests();
    }

    //공모전 id 리스트
    @GetMapping("/contest-list")    // 콘테스트의 ID를 보내줌?
    public ResponseEntity<List<Long>> contestIdList(){
        return ResponseEntity.ok(contestInfoService.getContestId());
    }

    @GetMapping("/contestdetail/{id}")
    public ResponseEntity<Contest> getContestDetail(@PathVariable Long id) {
        Contest contest = contestInfoService.findContestDetailById(id);
        if(contest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(contest, HttpStatus.OK);
        }
    }

    @PostMapping("/viewPlus")
    public ResponseEntity<String> increaseViewCount(@RequestBody Map<String, Long> viewsIncreaseRequest) {
        Long contestId = viewsIncreaseRequest.get("id");

        Contest contest = contestInfoService.findContestDetailById(contestId);
        if (contest == null) {
            return new ResponseEntity<>("Contest not found", HttpStatus.NOT_FOUND);
        } else {
            Long currentViews = contest.getViews();
            if (currentViews == null) {
                currentViews = 0L; // currentViews가 null이면 0으로 초기화
            }
            Long updatedViews = currentViews + 1;
            contest.setViews(updatedViews);
            contestInfoService.saveContest(contest);
            return new ResponseEntity<>("View count increased successfully", HttpStatus.OK);
        }
    }
}