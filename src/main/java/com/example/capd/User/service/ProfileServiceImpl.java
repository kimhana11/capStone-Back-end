package com.example.capd.User.service;

import com.example.capd.User.domain.Career;
import com.example.capd.User.domain.Profile;
import com.example.capd.User.domain.Review;
import com.example.capd.User.domain.User;
import com.example.capd.User.dto.CareerParam;
import com.example.capd.User.dto.ProfileRequestDto;
import com.example.capd.User.dto.ProfileResponseDto;
import com.example.capd.User.repository.CareerRepository;
import com.example.capd.User.repository.ProfileRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final CareerRepository careerRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public void saveProfile(ProfileRequestDto profileRequestDto) {
        //유저 존재 유무 확인
        Optional<User> userOptional = userRepository.findByUserId(profileRequestDto.getUserId());

        User user = userRepository.findByUserId(profileRequestDto.getUserId()).get();
        if (user.getProfile() != null) {
            throw new IllegalArgumentException("프로필이 이미 존재합니다. id=" + profileRequestDto.getUserId());
        }
        List<Career> careers = profileRequestDto.getCareers().stream()
                .map(CareerParam::toEntity)
                .collect(Collectors.toList());

        //평점은 기본 평점 3.5로 저장
        double rate = 3.5;
        Profile profile = profileRequestDto.toEntity(user);
        profile.setRate(rate);
        profileRepository.save(profile);
    }

    @Override
    public ProfileResponseDto getMyProfile(String userId) {
        Optional<Profile> profileOptional = profileRepository.findProfileAndCareersByUserId(userId);
        //유저 아이디로 프로필을 조회
        Profile profile = profileOptional.orElseThrow(() -> new EntityNotFoundException("프로필을 찾을 수 없습니다. id=" + userId));

        return mapToDTO(profile);
    }


    @Override
    public List<ProfileResponseDto> aiProfileList(String userId, Long contestId) {
       //ai 추천 리스트 줘야 구현 가능함
        return null;
    }

    @Override
    public void editProfile(ProfileRequestDto profileRequestDto) {
        Profile profile = profileRepository.findByUserId(profileRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("프로필이 존재하지 않습니다: " + profileRequestDto.getUserId()));

        Profile updatedProfile = profile.builder()
                .intro(profileRequestDto.getIntro())
                .stackList(profileRequestDto.getStackList())
                .Careers(profileRequestDto.getCareers().stream()
                        .map(CareerParam::toEntity)
                        .collect(Collectors.toList()))
                .build();

        profileRepository.save(updatedProfile);
    }

    @Override
    public void deleteProfile(String userId) {
        Long id = userRepository.findByUserId(userId).get().getId();
        profileRepository.deleteById(id);
    }

    private ProfileResponseDto mapToDTO(Profile profile) {
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setIntro(profile.getIntro());
        dto.setStackList(profile.getStackList());
        dto.setCareers(profile.getCareers().stream().map(this::mapCareerToDto).collect(Collectors.toList()));
        return dto;
    }

    private CareerParam mapCareerToDto(Career career) {
        CareerParam careerParam = new CareerParam();
        careerParam.setTitle(career.getTitle());
        careerParam.setStack(career.getStack());
        careerParam.setPeriod(career.getPeriod());
        careerParam.setGitHub(career.getGitHub());
        return careerParam;
    }

}
