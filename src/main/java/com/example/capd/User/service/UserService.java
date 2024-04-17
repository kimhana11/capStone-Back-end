package com.example.capd.User.service;

import com.example.capd.User.dto.SignRequest;
import com.example.capd.User.dto.SignResponse;

import com.example.capd.User.domain.User;
import com.example.capd.User.dto.UserDTO;
import com.example.capd.User.dto.UserUpdateRequest;
import com.example.capd.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //private final TokenProvider tokenProvider;

    //회원가입
    public Long save(UserDTO dto) {
        if (userRepository.findOneWithAuthoritiesByUsername(dto.getUserId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // 유저 정보를 만들어서 save
        User user = User.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .Email(dto.getEmail())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .Tendency(dto.getTendency())
                .Phone(dto.getPhone())
                .build();

        return userRepository.save(user).getId();
    }

    //정보수정
    public void updateUserInformation(UserUpdateRequest request) {
        User user = userRepository.findUserByUserId(request.getUserId());
        if (user != null) {
            user.setEmail(request.getNewEmail());
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUsername(request.getNewUsername());
            user.setGender(request.getNewGender());
            user.setAddress(request.getNewAddress());
            user.setTendency(request.getNewTendency());
            user.setPhone(request.getNewPhone());
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found with userId: " + request.getUserId());
        }
    }

    public SignResponse getUser(String userId) throws Exception {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(user);
    }

    //회원 탈퇴
    public void deleteUser(String userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다: " + userId);
        }
    }

//    public HashMap<String, Object> useridOverlap(Long id) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("result", userRepository.existsById(id));
//        return map;
//    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }
}
