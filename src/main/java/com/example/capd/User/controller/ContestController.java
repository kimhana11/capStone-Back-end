package com.example.capd.User.controller;

import com.example.capd.User.repository.ContestRepository;
import com.example.capd.User.service.ContestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;

    @GetMapping("/contest-list")
    public List<Long> contestIdList(){
        return contestService.getContestId();
    }
}
