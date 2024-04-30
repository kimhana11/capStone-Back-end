package com.example.capd.User.service;

import com.example.capd.Exception.UserWithDesiredStackNotFoundException;
import com.example.capd.User.domain.*;
import com.example.capd.User.dto.*;
import com.example.capd.User.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final CareerRepository careerRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;


    @Override
    public void saveProfile(ProfileRequestDto profileRequestDto) {
        //유저 존재 유무 확인
        Optional<User> userOptional = userRepository.findByUserId(profileRequestDto.getUserId());

        User user = userRepository.findByUserId(profileRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당하는 유저를 찾을 수 없습니다. userId=" + profileRequestDto.getUserId()));

        if (user.getProfile() != null) {
            throw new IllegalArgumentException("프로필이 이미 존재합니다. id=" + profileRequestDto.getUserId());
        }
        //평점은 기본 평점 3.5로 저장
        double rate = 3.5;
        Profile profile =profileRequestDto.toEntity(user);
        profile.setRate(rate);
        profileRepository.save(profile);

        List<Career> careers = profileRequestDto.getCareers() != null ?
                profileRequestDto.getCareers().stream()
                        .map(careerParam -> careerParam.toEntity(profile))
                        .collect(Collectors.toList()) :
                Collections.emptyList();


        careers.forEach(careerRepository::save);

    }

    @Override
    public ProfileResponseDto getMyProfile(String userId) {
        Optional<Profile> profileOptional = profileRepository.findProfileAndCareersByUserId(userId);
        //유저 아이디로 프로필을 조회
        Profile profile = profileOptional.orElseThrow(() -> new EntityNotFoundException("프로필을 찾을 수 없습니다. id=" + userId));

        return mapToDTO(profile);
    }

    @Override
    public List<ProfileParticipationRes> stackRecommendUsers(Long contestId, String userId) {

        Participation participation = participationRepository.findParticipationByContestIdAndUserId(contestId, userId);
        List<String> stackList = (participation != null) ? participation.getStackList() : Collections.emptyList();

        List<User> matchingUsers;

        matchingUsers = userRepository.findUsersByContestParticipation(contestId);

        //본일 프로필 제외,stackList 일치율 0인 사람은 제외, 일치울 높은순으로 정렬,팀 있는 유저 제외
        List<ProfileParticipationRes> resultProfiles = matchingUsers.stream()
                .filter(user ->
                        !user.getUserId().equals(userId) &&
                                user.getProfile() != null &&
                                user.getProfile().getStackList().stream().anyMatch(stackList::contains) &&
                                !hasTeamForContest(user, contestId)
                                )
                .sorted((user1, user2) ->
                        (int) user2.getProfile().getStackList().stream().filter(stackList::contains).count() -
                                (int) user1.getProfile().getStackList().stream().filter(stackList::contains).count())
                .map(user -> {
                    if (user.getProfile() != null) {
                        return mapToProfileParticipation(user.getProfile(),participation);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resultProfiles.isEmpty()) {
            throw new UserWithDesiredStackNotFoundException();
        }
        return resultProfiles;
    }
    // 해당 공모전에 팀이 없는지 확인하는 메서드
    private boolean hasTeamForContest(User user, Long contestId) {
        return user.getTeamMembers().stream().anyMatch(teamMember -> teamMember.getTeam().getContest().getId().equals(contestId));
    }

    @Override
    public List<ProfileResponseDto> aiRecommendUsers(Long contestId, String userId) {
       //******ai 추천 유저 리스트 필요*************8
        // JSON 파일에 저장할 데이터 생성
        return null;
    }

    @Override
    public void aiStart(Long contestId, Long userId) {
        // JSON 파일에 저장할 데이터 생성
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("contestId", contestId);
        jsonData.put("userId", userId);

        // JSON 파일 생성 및 저장
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data.json"), jsonData);
            System.out.println("JSON 파일이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            System.out.println("JSON 파일 생성 중 오류가 발생했습니다.");
            e.printStackTrace();
            return; // JSON 파일 생성 실패 시 메소드 종료
        }

        // 파이썬 스크립트 실행
        String pythonScriptPath = "C:/IntelliJ/Back-end/src/main/java/com/example/capd/ai/data.py";
        System.out.println("파이썬 스크립트를 실행합니다.");

        try {
            // 외부 프로세스로 파이썬 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            processBuilder.redirectErrorStream(true); // 에러 스트림을 표준 출력으로 리다이렉션
            Process process = processBuilder.start();

            // 파이썬 스크립트 실행이 완료될 때까지 대기
            int exitCode = process.waitFor();
            System.out.println("파이썬 스크립트 실행이 완료되었습니다. 종료 코드: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.out.println("파이썬 스크립트 실행 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }


    @Override
    public void editProfile(ProfileRequestDto profileRequestDto) {
        Optional<User> userOptional = userRepository.findByUserId(profileRequestDto.getUserId());
        Long userId = userOptional.map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. userId=" + profileRequestDto.getUserId()));

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("프로필이 존재하지 않습니다: " + userId));

        User user = userRepository.findByUserId(profileRequestDto.getUserId()).get();
        profileRepository.deleteById(profile.getId());

        Profile updatedProfile = Profile.builder()
                .id(profile.getId())
                .intro(profileRequestDto.getIntro())
                .stackList(profileRequestDto.getStackList())
                .rate(profile.getRate())    //기존의 프로필 별점을 그대로 적용 (안하면 0.0됨)
                .myTime(profileRequestDto.getMyTime())
                .desiredTime(profileRequestDto.getDesiredTime())
                .collaborationCount(profileRequestDto.getCollaborationCount())
                .user(user)
                .careers(profileRequestDto.getCareers().stream()
                        .map(careerParam -> {
                            if (careerParam.getId() != null) {
                                Career existingCareer = profile.getCareers().stream()
                                        .filter(c -> c.getId().equals(careerParam.getId()))
                                        .findFirst()
                                        .orElseThrow(() -> new EntityNotFoundException("경력이 존재하지 않습니다: " + careerParam.getId()));
                                existingCareer.builder()
                                        .title(careerParam.getTitle())
                                        .stack(careerParam.getStack())
                                        .period(careerParam.getPeriod())
                                        .gitHub(careerParam.getGitHub()).build();
                                return careerRepository.save(existingCareer);
                            } else {
                                Career newCareer = careerParam.toEntity(profile);
                                return careerRepository.save(newCareer);
                            }
                        })
                        .collect(Collectors.toList()))
                .build();

        profileRepository.save(updatedProfile);
    }



    @Override
    public void deleteProfile(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. userId=" + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            throw new EntityNotFoundException("프로필을 찾을 수 없습니다. userId=" + userId);
        }
        List<Career> careers = profile.getCareers();
        careerRepository.deleteAll(careers);
        profileRepository.deleteById(profile.getId());
    }

    private ProfileResponseDto mapToDTO(Profile profile) {
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getUserId());
        dto.setIntro(profile.getIntro());
        dto.setRate(profile.getRate());
        dto.setStackList(profile.getStackList());
        dto.setCareers(profile.getCareers().stream().map(this::mapCareerToDto).collect(Collectors.toList()));
        dto.setMyTime(profile.getMyTime());
        dto.setDesiredTime(profile.getDesiredTime());
        dto.setCollaborationCount(profile.getCollaborationCount());
        return dto;
    }


    private CareerParam mapCareerToDto(Career career) {
        CareerParam careerParam = new CareerParam();
        careerParam.setId(career.getId());
        careerParam.setTitle(career.getTitle());
        careerParam.setStack(career.getStack());
        careerParam.setPeriod(career.getPeriod());
        careerParam.setGitHub(career.getGitHub());
        return careerParam;
    }
    private ProfileParticipationRes mapToProfileParticipation(Profile profile, Participation participation){
        ProfileParticipationRes dto = new ProfileParticipationRes();
        dto.setId(profile.getUser().getId());
        dto.setUserId(profile.getUser().getUserId());
        dto.setIntro(profile.getIntro());
        dto.setRate(profile.getRate());
        dto.setStackList(profile.getStackList());
        dto.setAdditional(participation.getAdditional());
        dto.setTime(profile.getMyTime());
        dto.setCareers(profile.getCareers().stream().map(this::mapCareerToDto).collect(Collectors.toList()));
        return dto;
    }
}
