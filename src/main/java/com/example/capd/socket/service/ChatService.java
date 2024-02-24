package com.example.capd.socket.service;

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
public class ChatService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final TeamRepository teamRepository;
    private final ContestRepository contestRepository;


    public Long createRoom(ChatRoomDto chatRoomDto) {
        Team team = teamRepository.findById(chatRoomDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않습니다."));

        Room room = chatRoomDto.toEntity(team);
        roomRepository.save(room);

        return room.getId();
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        //메시지 -> 채팅방 -> 팀 순 삭제
        messageRepository.deleteAll(room.getMessages());
        roomRepository.deleteById(roomId);
        teamRepository.deleteById(room.getTeam().getId());
    }

    //팀 id로 채팅방 id 조회
    public Long getRoomId(Long teamId) {
        Room room = roomRepository.findByTeamId(teamId);
        Contest contest = contestRepository.findByTeams_Id(teamId);

        if (isReceptionPeriodEnded(contest.getReceptionPeriod())) {
            messageRepository.deleteAll(room.getMessages());
            //채팅방 삭제
            roomRepository.delete(room);
            teamRepository.deleteById(room.getTeam().getId());
            return null;
        }
        return room.getId();
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
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        List<Team> teams = teamRepository.findByMembersUserId(user.getId());

        List<Long> teamIds = teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        List<Room> rooms = roomRepository.findByTeamIdIn(teamIds);

        //ChatRoomDto 형태로 엔티티를 변환
        List<ChatRoomDto> chatRoomDtos = rooms.stream()
                .map(room -> {
                    return ChatRoomDto.builder()
                            .roomId(room.getId())
                            .password(room.getPassword())
                            .userId(userId)
                            .name(room.getName())
                            .teamId(room.getTeam().getId())
                            .build();
                })
                .collect(Collectors.toList());

        return chatRoomDtos;
    }

    //채팅방 비번 확인
    public Boolean checkRoomPw(RoomPwDto roomPwDto) {
        Room room = roomRepository.findById(roomPwDto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        if (room.getPassword().equals(roomPwDto.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    //이전 채팅 불러오기
    public List<MessageDto> getMessages(Long roomId) {
        List<Message> messages = messageRepository.findByRoomIdOrderByTimeStamp(roomId);
        List<MessageDto> messageDtoList = messages.stream()
                .map(message -> {
                    return MessageDto.builder()
                            .roomId(message.getId())
                            .senderId(message.getSenderId())
                            .message(message.getMessage())
                            .build();
                })
                .collect(Collectors.toList());
        return messageDtoList;
    }

    public void processMessage(WebSocketSession session, TextMessage message, Map<String, WebSocketSession> sessions) throws IOException {
        String sessionId = session.getId();
        ObjectMapper objectMapper = new ObjectMapper();

        // 메시지가 Chat 메시지인지, Cheer 메시지인지 확인
        if (isCheeringMessage(message)) {
            // Cheer 메시지 처리
            CheeringMessageDto cheeringMessageDto = objectMapper.readValue(message.getPayload(), CheeringMessageDto.class);
            saveCheeringMessage(cheeringMessageDto.getSenderId(), cheeringMessageDto.getMessage());

        } else if (isChatMessage(message)) {
            // Chat 메시지 처리
            MessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), MessageDto.class);
            Room room = roomRepository.findById(chatMessageDto.getRoomId()).orElse(null);
            if (room != null) {
                Message newMessage = chatMessageDto.toEntity(room);
                messageRepository.save(newMessage);
                // 다른 세션에 새로운 채팅 메시지 전송
                broadcastMessageToAll(sessionId, objectMapper.writeValueAsString(chatMessageDto), sessions);
            }
        }
    }

    // 채팅 메시지 여부 확인
    private boolean isChatMessage(TextMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(message.getPayload(), MessageDto.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 응원 메시지 여부 확인
    private boolean isCheeringMessage(TextMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(message.getPayload(), CheeringMessageDto.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 모든 세션에 메시지 브로드캐스트
    private void broadcastMessageToAll(String senderSessionId, String message, Map<String, WebSocketSession> sessions) {
        sessions.values().forEach((s) -> {
            if (!s.getId().equals(senderSessionId) && s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    // 예외 처리
                }
            }
        });
    }

    // 최대 저장할 응원 메시지 수
    private static final int MAX_CHEERING_MESSAGES = 20;

    // 응원 메시지를 저장할 list
    private final List<CheeringMessageDto> cheeringMessages = new ArrayList<>();

    private synchronized void saveCheeringMessage(String senderId, String message) {
        // 최대 저장할 응원 메시지 수를 초과하는 경우, 가장 오래된 메시지 삭제
        if (cheeringMessages.size() >= MAX_CHEERING_MESSAGES) {
            // 가장 오래된 메시지 삭제
            cheeringMessages.remove(0);
        }
        // 새로운 응원 메시지 추가
        cheeringMessages.add(new CheeringMessageDto(senderId, message));
    }
    public List<CheeringMessageDto> getCheeringMessage(){
        return cheeringMessages;
    }

}