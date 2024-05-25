package com.example.capd.User.controller;

import com.example.capd.User.dto.*;
import com.example.capd.User.domain.User;
import com.example.capd.User.repository.UserRepository;
import com.example.capd.User.service.AuthService;
import com.example.capd.User.service.UserService;
import com.example.capd.User.config.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map> getMemberProfile(
            @Validated @RequestBody LoginRequestDto request
    ) {
        String token = this.authService.login(request);
        String userId = request.getUserId(); // 클라이언트에게 전달할 사용자 아이디
        String id = String.valueOf(userRepository.findUserByUserId(userId).getId());
        String name = String.valueOf(userRepository.findUserByUserId(userId).getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", userId);
        response.put("id", id);
        response.put("name", name);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> join(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
        CommonResponse res = new CommonResponse(
                200,
                HttpStatus.OK,
                " 성공",
                null
        );
        return new ResponseEntity<>(res, res.getHttpStatus());
        //return userService.save(dto);
    }

    @PostMapping("/idCheck")
    public ResponseEntity<CommonResponse> checkUserId(@RequestBody UserIdRequest request) {
        String userId = request.getUserId();
        Optional<User> userOptional = userRepository.findFirstByUserId(userId);
        CommonResponse res;
        if (userOptional.isPresent()) {
            res = new CommonResponse(400, HttpStatus.BAD_REQUEST, "아이디가 이미 존재합니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } else {
            res = new CommonResponse(200, HttpStatus.OK, "사용 가능한 아이디입니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<SignResponse> getUser(@RequestParam String userId) throws Exception {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request,response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUserInformation(@RequestBody UserUpdateRequest request) {
        userService.updateUserInformation(request);
        return ResponseEntity.status(HttpStatus.OK).body("User information updated successfully.");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + userId);
        }
    }
}
