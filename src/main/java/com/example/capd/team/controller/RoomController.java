package com.example.capd.team.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.team.dto.ChatRoomDto;
import com.example.capd.team.dto.RoomPwDto;
import com.example.capd.socket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;

    //방 저장
    @PostMapping("/chat-room")
    public ResponseEntity<CommonResponse> saveChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        chatService.createRoom(chatRoomDto);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "저장 성공",
                null
        );
       return new ResponseEntity<>(res, res.getHttpStatus());
    }


    @GetMapping("/chat-room/{teamId}")
    public ResponseEntity<CommonResponse> RoomId(@PathVariable Long teamId) {
        CommonResponse res;
        Long roomId =  chatService.getRoomId(teamId);
        if(roomId!=null){
            res = new CommonResponse(
                    200,
                    HttpStatus.OK,
                    "조회 성공",
                    roomId
            );
        }else {
            res = new CommonResponse(
                    400,
                    HttpStatus.NOT_FOUND,
                    "공모 기간이 끝나 삭제된 채팅방입니다.",
                    null
            );
        }
        return new ResponseEntity<>(res, res.getHttpStatus());
        }


    @GetMapping("/room-list/{userId}")
    public List<ChatRoomDto> RoomList(@PathVariable String userId) {
       return chatService.getRoomList(userId);
    }

    @PostMapping("/chat-room/password")
    public ResponseEntity<CommonResponse> checkRoomPw(@RequestBody RoomPwDto roomPwDto){
        Boolean checkPw = chatService.checkRoomPw(roomPwDto);

        CommonResponse res;
        if(checkPw){
            res = new CommonResponse(
                    200,
                    HttpStatus.OK,
                    "비밀번호 인증 성공",
                    null
            );
        }else{
            res = new CommonResponse(
                    400,
                    HttpStatus.NOT_FOUND,
                    "비밀번호 인증 실패",
                    null
            );
        }
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
    //방삭제
    @DeleteMapping("delete-room/{roomId}")
    public ResponseEntity<CommonResponse> deleteRoom(@PathVariable Long roomId){
        chatService.deleteRoom(roomId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
