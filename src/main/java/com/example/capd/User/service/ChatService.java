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

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;


    public Long createRoom(ChatRoomDto chatRoomDto) {
        User leader = userRepository.findByUserId(chatRoomDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        Team team = teamRepository.findById(chatRoomDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("팀이 존재하지 않습니다."));

        Room room = chatRoomDto.toEntity(team);
        roomRepository.save(room);

        return room.getId();
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
    //두 세션이 동일한 룸에 있는지 확인
    private boolean isSameRoom(String sessionId1, WebSocketSession session2, Long roomId) {
        return sessionId1.equals(session2.getId()) || roomId.equals(getRoomId(session2));
    }

    //세션과 연결된 룸 ID
    private Long getRoomId(WebSocketSession session) {
        Object roomIdAttribute = session.getAttributes().get("roomId");
        return roomIdAttribute instanceof Long ? (Long) roomIdAttribute : null;
    }
}
