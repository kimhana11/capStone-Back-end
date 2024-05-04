package com.example.capd.contest.service;

import com.example.capd.contest.domain.Contest;
import com.example.capd.contest.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ContestInfoService {

    private final ContestRepository contestRepository;

    public List<Map<String, Object>> fetchDataFromDatabase() {
        return contestRepository.fetchDataFromDatabase();
    }
    // Contest 정보를 출력
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    // 공모전 id 리스트 전체 출력
    public List<Long> getContestId(){
        return contestRepository.findAllIds();
    }

    // 공모전 id로 해당 공모전 정보 반환
    public Contest findContestDetailById(Long id) {
        return contestRepository.findByIdWithoutParticipations(id);
    }

    // 업데이트된 공모전 정보 저장 (예: 조회수증가)
    public Contest saveContest(Contest contest) {
        return contestRepository.save(contest);
    }
}