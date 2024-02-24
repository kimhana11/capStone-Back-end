package com.example.capd.socket.config;

import com.example.capd.socket.dto.CheeringMessageDto;
import com.example.capd.socket.dto.MessageDto;
import com.example.capd.socket.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();
    private final Map<String, Set<WebSocketSession>> roomSubscribers = new HashMap<>();
    private final Deque<CheeringMessageDto> cheeringMessagesQueue = new ArrayDeque<>(); //응원글 저장할
    private static final Logger logger = LoggerFactory.getLogger(MyHandler.class);

    private final ChatService chatService;

    public MyHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    //최초 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String sessionId = session.getId();

        Map<String, String> response = new HashMap<>();
        response.put("type", "welcome");
        response.put("message", "연결이 성공적으로 되었습니다.");

        // Convert the response to JSON
        String jsonResponse = new ObjectMapper().writeValueAsString(response);

        // Send the JSON response
        session.sendMessage(new TextMessage(jsonResponse));

        // 특정 사용자 그룹에 메시지를 보낼
        // 사용자와 연결된 roomId 있는 경우 동일한 roomId를 가진 사용자에게 메시지를 브로드캐스트
        Long roomId = (Long) session.getAttributes().get("roomId");
        if (roomId != null) {
            //  sendChatHistoryToUser(session, roomId);
            String groupMessage = "새로운 유저가 팀에 합류했습니다.";
            broadcastMessageToRoom(groupMessage, roomId);
        }

        // 세션 저장
        sessions.put(sessionId, session);

        //연결된 모든 세션에 입장 메시지
        sessions.values().forEach((s) -> {
            try {
                if (!s.getId().equals(sessionId) && s.isOpen()) {
                    s.sendMessage(new TextMessage(jsonResponse));
                }
            } catch (IOException e) {
                logger.error("Exception during connection establishment: {}", e.getMessage());
            }
        });
    }

    //채팅방 구독
    private void subscribeToRoom(WebSocketSession session, Long roomId) {
        roomSubscribers.computeIfAbsent(String.valueOf(roomId), k -> new HashSet<>()).add(session);
    }

    //양방향 데이터 통신할 떄 해당 메서드가 call 된다.
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        //구독 메시지 확인
        if (payload.startsWith("/sub/chat/")) {
            // 구독 메시지에서 roomId 추출
            Long roomId = Long.valueOf(payload.substring("/sub/chat/".length()));
            // 세션을 해당 방에 구독
            subscribeToRoom(session, roomId);
            sendChatHistoryToUser(session, roomId);
        }
        // 발행 메시지 확인
        else if (payload.startsWith("/pub/chat/")) {
            // 발행 메시지 처리
            handlePublicationMessage(session, message, sessions);
        }
        //실시간 응원글
        else if (payload.equals("/cheer")) {
            sendCheeringMessagesToAll();
        }
        // 기타 메시지는 기존 방식으로 처리
        else {
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
    //발행 메시지를 처리하는 메서드
    public void handlePublicationMessage(WebSocketSession session, TextMessage message, Map<String, WebSocketSession> sessions) {
        String payload = message.getPayload();
        //발행 메시지에서 roomId 추출
        Long roomId = Long.valueOf(payload.substring("/pub/chat/".length()));

        // 메시지를 구독자에게 브로드 캐스트
        String publicationMessage = "채팅방에 메시지를 전송했습니다. " + roomId;
        broadcastMessageToRoom(publicationMessage, roomId);
    }

    //이전 채팅 내용을 조회
    private void sendChatHistoryToUser(WebSocketSession session, Long roomId) {
        List<MessageDto> chatHistory = chatService.getMessages(roomId);

        JSONArray chatHistoryArray = new JSONArray();
        for (MessageDto message : chatHistory) {
            JSONObject messageObject = new JSONObject();
            messageObject.put("senderId", message.getSenderId());
            messageObject.put("message", message.getMessage());
            chatHistoryArray.add(messageObject);
        }

        JSONObject historyObject = new JSONObject();
        historyObject.put("type", "chatHistory");
        historyObject.put("content", chatHistoryArray);

        System.out.println("Chat History Content: " + historyObject.toJSONString());

        try {
            // Send the formatted chat history to the user
            session.sendMessage(new TextMessage(historyObject.toJSONString()));
        } catch (IOException e) {
            // Handle exception as needed
        }
    }


    // 모든 클라이언트에게 응원 메시지 전송 메서드
    private void sendCheeringMessagesToAll() {
        String jsonMessages = convertCheeringMessagesToJson();
        sessions.values().forEach((session) -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessages));
                } catch (IOException e) {
                    logger.error("Error sending cheering messages to all: {}", e.getMessage());
                }
            }
        });
    }

    private List<CheeringMessageDto> cheeringMessages = new ArrayList<>();

    // 응원 메시지를 JSON 형식으로 변환하는 메서드
    private String convertCheeringMessagesToJson() {
        cheeringMessages = chatService.getCheeringMessage();
        // List를 JSON 형식의 문자열로 변환하여 반환
        String jsonMessages = cheeringMessages.stream()
                .map(message -> "{ \"senderId\": \"" + message.getSenderId() + "\", \"message\": \"" + message.getMessage() + "\" }")
                .collect(Collectors.joining(",", "[", "]"));
        return jsonMessages;
    }
}