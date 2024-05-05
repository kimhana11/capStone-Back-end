package com.example.capd.team.service;

import com.example.capd.Exception.MemberLimitExceededException;
import com.example.capd.Exception.TeamAlreadyConfirmedException;
import com.example.capd.Exception.UnauthorizedTeamMemberModificationException;
import com.example.capd.contest.repository.ContestRepository;
import com.example.capd.socket.domain.Message;
import com.example.capd.socket.repository.MessageRepository;
import com.example.capd.team.domain.*;
import com.example.capd.team.repository.*;
import com.example.capd.User.domain.*;
import com.example.capd.team.dto.*;;
import com.example.capd.socket.dto.*;
import com.example.capd.User.dto.*;;
import com.example.capd.User.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.example.capd.contest.domain.Contest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final ContestRepository contestRepository;
    private final TeamMemberRepository teamMemberRepository;


    public void createRoom(ChatRoomDto chatRoomDto) {

        List<String> memberIds = chatRoomDto.getMemberIds();
        Long contestId = chatRoomDto.getContestId();

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공모전 입니다: " + contestId));

        if(memberIds.size()>6){
            throw new MemberLimitExceededException();
        }

        //방 생성
        Room room = chatRoomDto.toEntity(contest);
        room.setStatus(false);
        room = roomRepository.save(room);
        Room finalTeam = room;
        List<TeamMember> teamMembers = userRepository.findAllByUserIdIn(memberIds)
                .stream()
                .map(user -> TeamMember.fromUserAndTeam(user, finalTeam))
                .collect(Collectors.toList());

        teamMemberRepository.saveAll(teamMembers);
    }


    // 채팅방 단일 조회
    public ChatRoomDto getRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다. userId=" + roomId));
        Contest contest = contestRepository.findByRooms_Id(roomId);
        //공모전
//        if (isReceptionPeriodEnded(contest.getReceptionPeriod())) {
//            messageRepository.deleteAll(room.getMessages());
//            //채팅방 삭제
//            roomRepository.delete(room);
//            return null;
//        }
        return mapToDto(room);
    }

    //방 확정 상태 변경
    public void updateRoomStatus(Long roomId, Boolean newStatus, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        User modifyingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + userId));

        if (!modifyingUser.getUserId().equals(room.getLeaderId())) {
            throw new UnauthorizedTeamMemberModificationException();
        }
        if (room.getStatus() != null && room.getStatus()) {
            throw new TeamAlreadyConfirmedException();
        }

        room.setStatus(newStatus);
        roomRepository.save(room);
    }

    private boolean isReceptionPeriodEnded(String receptionPeriod) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss");
        String[] period = receptionPeriod.split("~");

        LocalDateTime startDate = LocalDateTime.parse(period[0] + "T00:00:00", formatter);
        LocalDateTime endDate = LocalDateTime.parse(period[1] + "T23:59:59", formatter);
        LocalDateTime currentTime = LocalDateTime.now();


        return currentTime.isEqual(endDate) || currentTime.isAfter(endDate);
    }

    //유저가 속한 채팅방 전체 조회
    public List<ChatRoomDto> getRoomList(String userId) {

        List<Room> rooms = roomRepository.findAllByMembersUserUserId(userId);

        return rooms.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    //공모전에 생성된 채팅방 전체 조회
    public List<ChatRoomDto> contestRoomList(Long contestId) {
        List<Room> rooms = roomRepository.findByContestId(contestId);
        return rooms.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        //메시지 -> 채팅방 순 삭제
        messageRepository.deleteAll(room.getMessages());
        roomRepository.deleteById(roomId);
    }

    //팀 멤버 수정
    public void addMembersToTeam(Long roomId, List<String> memberIds, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다 "));

        User modifyingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + userId));

        if (!modifyingUser.getUserId().equals(room.getLeaderId())) {
            throw new UnauthorizedTeamMemberModificationException();
        }
        if (room.getStatus() != null && room.getStatus()) {
            throw new TeamAlreadyConfirmedException();
        }

        List<TeamMember> teamMembers = new ArrayList<>();

        for (String memberId : memberIds) {
            User user = userRepository.findByUserId(memberId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저 아이디: " + memberId));

            // 이미 저장되어 있는 멤버인지 확인
            boolean isMember = room.getMembers().stream()
                    .anyMatch(member -> member.getUser().equals(user));

            if (!isMember) {
                TeamMember teamMember = TeamMember.builder()
                        .user(user)
                        .room(room)
                        .build();
                teamMembers.add(teamMember);
            }
        }

        room.getMembers().addAll(teamMembers);
        roomRepository.save(room);
    }

    private ChatRoomDto mapToDto(Room room) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.setRoomId(room.getId());
        chatRoomDto.setName(room.getName());
        chatRoomDto.setLeaderId(room.getLeaderId());
        chatRoomDto.setContestId(room.getContest().getId());

        List<String> stringMemberIds = room.getMembers().stream()
                .map(teamMember -> teamMember.getUser().getUserId())
                .collect(Collectors.toList());
        chatRoomDto.setMemberIds(stringMemberIds);

        Message lastMessage = room.getLastMessage();
        if (lastMessage != null) {
            chatRoomDto.setLastMessage(lastMessage.getMessage());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTimestamp = lastMessage.getTimeStamp().format(formatter);
            chatRoomDto.setLastMessageTimeStamp(formattedTimestamp);
        }
        return chatRoomDto;
    }
}