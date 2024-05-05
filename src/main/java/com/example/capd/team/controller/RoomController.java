package com.example.capd.team.controller;

import com.example.capd.User.config.CommonResponse;
import com.example.capd.team.dto.ChatRoomDto;
import com.example.capd.socket.service.ChatService;
import com.example.capd.team.dto.RoomParam;
import com.example.capd.team.service.RoomService;
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

    private final RoomService roomService;

    //방 저장
    @PostMapping("/room")
    public ResponseEntity<CommonResponse> saveChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        roomService.createRoom(chatRoomDto);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "저장 성공",
                null
        );
       return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //단일 조회
    @GetMapping("/room/{roomId}")
    public ChatRoomDto RoomId(@PathVariable Long roomId) {
        return roomService.getRoom(roomId);

    }

    //userId가 속한 채팅방 전체 조회
    @GetMapping("/rooms/{userId}")
    public List<ChatRoomDto> RoomList(@PathVariable String userId) {
       return roomService.getRoomList(userId);
    }

    @GetMapping("/rooms-contest/{contestId}")
    public List<ChatRoomDto> getContestRoomList(@PathVariable Long contestId){
        return roomService.contestRoomList(contestId);
    }

    @PostMapping("/room-update/status")
    public ResponseEntity<CommonResponse> updateStatus(@RequestBody RoomParam roomParam){
        roomService.updateRoomStatus(roomParam.getRoomId(),roomParam.getStatus(), roomParam.getUserId());
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/room-update/members")
    public ResponseEntity<CommonResponse> updateTeamMember(@RequestBody RoomParam roomParam){
        roomService.addMembersToTeam(roomParam.getRoomId(),roomParam.getMemberIds(), roomParam.getUserId());
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "수정 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    //방삭제
    @DeleteMapping("delete-room/{roomId}")
    public ResponseEntity<CommonResponse> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                "삭제 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
    }
}
