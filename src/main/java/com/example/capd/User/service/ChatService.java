package com.example.capd.User.service;

import com.example.capd.User.domain.*;
import com.example.capd.User.dto.ChatRoomDto;
import com.example.capd.User.dto.MessageDto;
import com.example.capd.User.repository.MessageRepository;
import com.example.capd.User.repository.RoomRepository;
import com.example.capd.User.repository.TeamRepository;
import com.example.capd.User.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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


    public Long createRoom(ChatRoomDto chatRoomDto) {
        Team team = teamRepository.findById(chatRoomDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않습니다."));

        Room room = chatRoomDto.toEntity(team);
        roomRepository.save(room);

        return room.getId();
    }


    //팀 id로 채팅방 id 조회
    public Long getRoomId(Long teamId){
        Room room =roomRepository.findByTeamId(teamId);
        return room.getId();
    }


    //유저가 속한 채팅방 전체 조회
//    public List<Room> getRoomList(String userId){
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));
//
//        List<Team> teams = teamRepository.findByMembersUserId(user.getId());
//        List<Long> teamIds = teams.stream()
//                .map(Team::getId)
//                .collect(Collectors.toList());
//
//        return roomRepository.findByTeamIdIn(teamIds);
//    }

    //채팅방 비번 확인

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
