package com.example.capd.User.service;

import com.example.capd.Exception.AlreadyAppliedException;
import com.example.capd.User.domain.Participation;
import com.example.capd.User.domain.User;
import com.example.capd.User.dto.ContestDto;
import com.example.capd.User.dto.ParticipationParam;
import com.example.capd.contest.repository.ContestRepository;
import com.example.capd.User.repository.ParticipationRepository;
import com.example.capd.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.capd.contest.domain.Contest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;

    public void saveParticipation(ParticipationParam participationParam){

        String userId = participationParam.getUserId();
        Long contestId = participationParam.getContestId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. id=" + userId));
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("공모전을 찾을 수 없습니다. id=" + contestId));

        Participation participationExists = participationRepository.findParticipationByContestIdAndUserId(contestId, user.getId());
        if (participationExists !=null) {
            throw new AlreadyAppliedException();
        }

        Participation participation = participationParam.toEntity(user, contest);
        participationRepository.save(participation);
    }

    public void deleteParticipation(Long contestId,String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. id=" + userId));
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(()-> new EntityNotFoundException("해당 공모전을 찾을 수 없습니다. 현황을 확인하세요="+contestId));

        Participation participation = participationRepository.findParticipationByContestIdAndUserId(contestId, user.getId());

        participationRepository.deleteById(participation.getId());
    }

    public List<ContestDto> myContestList(String userId){
        User user = userRepository.findUserByUserId(userId);
        List<Participation> participations = participationRepository.findParticipationsByUserId(user.getId());

        return participations.stream()
                .map(participation -> {
                    Contest contest = participation.getContest();
                    ContestDto contestDto = ContestDto.builder().
                            contestId(contest.getId()).title(contest.getTitle()).build();
                    return contestDto;
                })
                .collect(Collectors.toList());
    }
}
