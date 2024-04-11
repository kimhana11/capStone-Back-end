package com.example.capd.contest.service;

import com.example.capd.contest.domain.Contest;
import com.example.capd.contest.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.json.simple.*;
import java.io.*;

@Service
@RequiredArgsConstructor
public class CrawlingScriptService {

    private final ContestRepository contestRepository; // ContestRepository를 주입받음

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 24시간마다 실행
    public void executeCrawlingScript() {
        try {
            // 파이썬 스크립트 경로
            String pythonScriptPath = "C:/Users/jwj/Desktop/Back-end/src/main/java/com/example/capd/python/Crawling_START.py";

            System.out.println("파이썬 실행");
            // 외부 프로세스로 파이썬 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            processBuilder.redirectErrorStream(true); // 에러 스트림을 표준 출력으로 리다이렉션
            Process process = processBuilder.start();
            System.out.println("파이썬 실행 완");

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
            System.out.println("파이썬 스크립트 실행 종료, 종료 코드: " + exitCode);

            System.out.println("contestKorea json 읽어옴"); //여기서 종료 -> 파이썬 수정완
            try (FileReader reader = new FileReader("contestkorea_contest_data.json")) {
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("contestkorea_contest_data.json"));

                // 각 JSON 객체를 Contest 엔티티로 변환하여 저장
                for (Object json : jsonArray) {
                    JSONObject jsonObj = (JSONObject) json;
                    String contestTitle = (String) jsonObj.get("contest_title");

                    // 데이터베이스에 해당 title이 이미 존재하는지 확인
                    Contest existingContest = contestRepository.findByTitle(contestTitle);
                    if (existingContest != null) {
                        // 이미 존재하는 title이면 패스
                        System.out.println("이미 존재하는 대회입니다: " + contestTitle);
                        continue;
                    }

                    // 새로운 title이라면 엔티티 저장
                    Contest contest = Contest.builder()
                            .title((String) jsonObj.get("contest_title"))
                            .host((String) jsonObj.get("contest_host"))
                            .targetParticipants((String) jsonObj.get("contest_target_participants"))
                            .receptionPeriod((String) jsonObj.get("contest_reception_period"))
                            .decisionPeriod((String) jsonObj.get("contest_decision_period"))
                            .compatitionArea((String) jsonObj.get("contest_compatition_area"))
                            .award((String) jsonObj.get("contest_award"))
                            .homepage((String) jsonObj.get("contest_homepage"))
                            .howToApply((String) jsonObj.get("contest_how_to_apply"))
                            .fee((String) jsonObj.get("contest_fee"))
                            .image((String) jsonObj.get("contest_image"))
                            .detailText((String) jsonObj.get("contest_detail_text"))
                            .build();

                    contestRepository.save(contest);
                    System.out.println("엔티티 저장 완");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            System.out.println("wevity json 읽어옴"); //여기서 종료 -> 파이썬 수정완
            try (FileReader reader = new FileReader("wevity_contest_data.json")) {
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("wevity_contest_data.json"));

                // 각 JSON 객체를 Contest 엔티티로 변환하여 저장
                for (Object json : jsonArray) {
                    JSONObject jsonObj = (JSONObject) json;
                    String contestTitle = (String) jsonObj.get("contest_title");

                    // 데이터베이스에 해당 title이 이미 존재하는지 확인
                    Contest existingContest = contestRepository.findByTitle(contestTitle);
                    if (existingContest != null) {
                        // 이미 존재하는 title이면 패스
                        System.out.println("이미 존재하는 대회입니다: " + contestTitle);
                        continue;
                    }

                    // 새로운 title이라면 엔티티 저장
                    Contest contest = Contest.builder()
                            .title((String) jsonObj.get("contest_title"))
                            .host((String) jsonObj.get("contest_host"))
                            .targetParticipants((String) jsonObj.get("contest_target_participants"))
                            .receptionPeriod((String) jsonObj.get("contest_reception_period"))
                            .decisionPeriod((String) jsonObj.get("contest_decision_period"))
                            .compatitionArea((String) jsonObj.get("contest_compatition_area"))
                            .award((String) jsonObj.get("contest_award"))
                            .homepage((String) jsonObj.get("contest_homepage"))
                            .howToApply((String) jsonObj.get("contest_how_to_apply"))
                            .fee((String) jsonObj.get("contest_fee"))
                            .image((String) jsonObj.get("contest_image"))
                            .detailText((String) jsonObj.get("contest_detail_text"))
                            .build();

                    contestRepository.save(contest);
                    System.out.println("엔티티 저장 완");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}