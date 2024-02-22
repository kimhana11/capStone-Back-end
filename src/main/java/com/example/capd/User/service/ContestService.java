package com.example.capd.User.service;

import com.example.capd.User.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;

    public List<Long> getContestId(){
        return contestRepository.findAllIds();
    }
}
