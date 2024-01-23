package com.example.capd.User.controller;

import com.example.capd.User.domain.Room;
import com.example.capd.User.dto.ChatRoomDto;
import com.example.capd.User.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;

    //방 저장
    @PostMapping("/chatRoom")
    public void saveChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        chatService.createRoom(chatRoomDto);
    }


    @GetMapping("/chatRoom/{teamId}")
    public Long RoomId(@PathVariable Long teamId) {
       return chatService.getRoomId(teamId);
    }

//    @GetMapping("/RoomList/{userId}")
//    public List<Room> RoomList(@PathVariable String userId) {
//       return chatService.getRoomList(userId);
//    }
}
