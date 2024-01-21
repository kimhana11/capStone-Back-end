package com.example.capd.User.controller;

import com.example.capd.User.dto.ChatRoomDto;
import com.example.capd.User.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @PostMapping ("/chatRoom")
    public void saveChatRoom(@RequestBody ChatRoomDto chatRoomDto){
        chatService.createRoom(chatRoomDto);
    }


}
