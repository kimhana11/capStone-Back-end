package com.example.capd.socket.service;

import com.example.capd.User.domain.User;
import com.example.capd.contest.repository.ContestRepository;
import com.example.capd.socket.config.MyHandler;
import com.example.capd.socket.domain.Message;
import com.example.capd.socket.repository.MessageRepository;
import com.example.capd.team.domain.*;
import com.example.capd.team.repository.*;
import com.example.capd.socket.dto.*;
import com.example.capd.User.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Logger logger = LoggerFactory.getLogger(MyHandler.class);
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    //이전 채팅 불러오기
    public List<MessageDto> getMessages(Long roomId) {
        List<Message> messages = messageRepository.findByRoomIdOrderByTimeStamp(roomId);
        List<MessageDto> messageDtoList = messages.stream()
                .map(message -> {
                    Optional<User> sender = userRepository.findByUserId(message.getSenderId());
                    return MessageDto.builder()
                            .roomId(message.getId())
                            .senderId(sender.get().getUsername())
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
            saveCheeringMessage(cheeringMessageDto.getSenderName(), cheeringMessageDto.getMessage());
            broadcastCheeringMessages(sessions);
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

    private void broadcastCheeringMessages(Map<String, WebSocketSession> sessions) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CheeringMessageDto> cheeringMessages = getCheeringMessage();
        String messagePayload;
        try {
            Map<String, Object> messageWrapper = new HashMap<>();
            messageWrapper.put("type", "cheer");
            messageWrapper.put("cheeringMessages", cheeringMessages);
            messagePayload = objectMapper.writeValueAsString(messageWrapper);

        } catch (IOException e) {
            return;
        }

        for (WebSocketSession s : sessions.values()) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(messagePayload));
                } catch (IOException e) {
                }
            }
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
    private static final int MAX_CHEERING_MESSAGES = 5;

    // 응원 메시지를 저장할 list
    private final List<CheeringMessageDto> cheeringMessages = new ArrayList<>();

    private synchronized void saveCheeringMessage(String senderName, String message) {
        // 최대 저장할 응원 메시지 수를 초과하는 경우, 가장 오래된 메시지 삭제
        if (cheeringMessages.size() >= MAX_CHEERING_MESSAGES) {
            // 가장 오래된 메시지 삭제
            cheeringMessages.remove(0);
        }
        // 새로운 응원 메시지 추가
        cheeringMessages.add(new CheeringMessageDto(senderName, message));
    }
    public List<CheeringMessageDto> getCheeringMessage(){
        return cheeringMessages;
    }

}