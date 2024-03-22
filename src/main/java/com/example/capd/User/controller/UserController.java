package com.example.capd.User.controller;

import com.example.capd.User.dto.*;
import com.example.capd.User.domain.User;
import com.example.capd.User.repository.UserRepository;
import com.example.capd.User.service.UserService;
import com.example.capd.User.config.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;




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



    //로그인 하면 토큰과 함께, id, username 프론트에 전달
    @PostMapping("/login")
    public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
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

    @GetMapping("/profile-update")
    public String showEditForm(Model model, Authentication authentication) {
        UserService currentUser = (UserService) authentication.getPrincipal();
        model.addAttribute("currentUser", currentUser);
        return "profile-update";
    }

    @PostMapping("/profile-update")
    public String editUserInformation(@ModelAttribute("currentUser") User currentUser,
                                      @RequestParam("newEmail") String newEmail,
                                      @RequestParam("newPassword") String newPassword) {
        userService.updateUserInformation(currentUser.getUsername(), newEmail, newPassword);
        return "redirect:/user/profile-update?success";
    }

    @GetMapping("/delete")
    public String showDeleteForm(Model model, Authentication authentication) {
        UserService currentUser = (UserService) authentication.getPrincipal();
        model.addAttribute("currentUser", currentUser);
        return "deleteUserForm";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("currentUser") User currentUser) {
        userService.deleteUser(currentUser.getUsername());
        return "redirect:/logout";
    }
}
