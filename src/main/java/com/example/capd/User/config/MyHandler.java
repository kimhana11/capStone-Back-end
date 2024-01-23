package com.example.capd.User.config;


import com.example.capd.User.service.ChatService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class MyHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final Map<String, Set<WebSocketSession>> roomSubscribers = new HashMap<>();
    private final ChatService chatService;

    public MyHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    //최초 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String sessionId = session.getId();

        // 연결된 사용자에게 시작 메시지 보내기
        String welcomeMessage = "연결이 성공적으로 되었습니다.";
        session.sendMessage(new TextMessage(welcomeMessage));

        // 특정 사용자 그룹에 메시지를 보낼
        // 사용자와 연결된 roomId 있는 경우 동일한 roomId를 가진 사용자에게 메시지를 브로드캐스트
        Long roomId = (Long) session.getAttributes().get("teamId");
        if (roomId != null) {
            String groupMessage = "새로운 유저가 팀에 합류했습니다.";
            broadcastMessageToRoom(groupMessage, roomId);
        }

        // 세션 저장
        sessions.put(sessionId, session);

        //연결된 모든 세션에 입장 메시지
        sessions.values().forEach((s) -> {
            try {
                if (!s.getId().equals(sessionId) && s.isOpen()) {
                    s.sendMessage(new TextMessage(welcomeMessage));
                }
            } catch (IOException e) {
            }
        });
    }


    private void subscribeToRoom(WebSocketSession session, Long roomId) {
        roomSubscribers.computeIfAbsent(String.valueOf(roomId), k -> new HashSet<>()).add(session);
    }
    //양방향 데이터 통신할 떄 해당 메서드가 call 된다.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if (payload.startsWith("/subscribe ")) {
            // Extract room ID from the subscription message
            Long roomId = Long.valueOf(payload.substring("/subscribe ".length()));
            // Subscribe the session to the room
            subscribeToRoom(session, roomId);
        } else {
            // Process other messages as usual
            chatService.processMessage(session, message, sessions);
        }
    }
    //웹소켓 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String sessionId = session.getId();
        final String leaveMessage = sessionId + "님이 떠났습니다.";
        sessions.remove(sessionId); // 삭제

        //메시지 전송
        sessions.values().forEach((s) -> {

            if (!s.getId().equals(sessionId) && s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(leaveMessage));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    //통신 에러 발생 시
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    private void broadcastMessageToRoom(String message, Long roomId) {
        roomSubscribers.getOrDefault(roomId, Collections.emptySet()).forEach(s -> {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    // Handle exception as needed
                }
            }
        });

    }

}