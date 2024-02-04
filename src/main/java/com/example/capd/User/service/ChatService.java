package com.example.capd.User.service;

import com.example.capd.User.domain.*;
import com.example.capd.User.dto.ChatRoomRequestDto;;
import com.example.capd.User.dto.ChatRoomResponseDto;
import com.example.capd.User.dto.MessageDto;
import com.example.capd.User.dto.RoomPwDto;
import com.example.capd.User.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;


    public Long createRoom(ChatRoomRequestDto chatRoomDto) {
        Team team = teamRepository.findById(chatRoomDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않습니다."));

        Room room = chatRoomDto.toEntity(team);
        roomRepository.save(room);

        return room.getId();
    }

    public void deleteRoom(Long roomId){
        Room room =roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        //메시지 -> 채팅방 -> 팀 순 삭제
        messageRepository.deleteAll(room.getMessages());
        roomRepository.deleteById(roomId);
        teamRepository.deleteById(room.getTeam().getId());
    }

    //팀 id로 채팅방 id 조회
    public Long getRoomId(Long teamId){
        Room room =roomRepository.findByTeamId(teamId);
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
    public List<ChatRoomResponseDto> getRoomList(String userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        List<Team> teams = teamRepository.findByMembersUserId(user.getId());

        List<Long> teamIds = teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        List<Room> rooms = roomRepository.findByTeamIdIn(teamIds);

        //ChatRoomDto 형태로 엔티티를 변환
        List<ChatRoomResponseDto> chatRoomDtos = rooms.stream()
                .map(room -> {
                    return ChatRoomResponseDto.builder()
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
    public Boolean checkRoomPw(RoomPwDto roomPwDto){
        Room room = roomRepository.findById(roomPwDto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        if(room.getPassword().equals(roomPwDto.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    //이전 채팅 불러오기
    public List<MessageDto> getMessages(Long roomId){
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
        MessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), MessageDto.class);

        Room room = roomRepository.findById(chatMessageDto.getRoomId()).orElse(null);
        if (room != null) {
            Message newMessage = chatMessageDto.toEntity(room);
            messageRepository.save(newMessage);

            sessions.values().forEach((s) -> {
                if (!s.getId().equals(sessionId) && s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessageDto)));
                    } catch (IOException e) {

                    }
                }
            });
        }
    }


}
