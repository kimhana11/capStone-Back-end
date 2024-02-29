package com.example.capd.User.service;

import com.example.capd.User.dto.UserDTO;
import com.example.capd.User.dto.UserSignInDto;
import com.example.capd.User.dto.UserSignInResponseDto;
import com.example.capd.User.domain.User;
import com.example.capd.User.JWT.TokenProvider;
import com.example.capd.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    //회원가입
    public Long save(UserDTO dto) {
        String encodedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        User user = dto.toEntity();
        userRepository.save(user);

        return user.getId();
    }

    //로그인
    public UserSignInResponseDto login(UserSignInDto userSignInDto) {

        //userName로 유저 조회
        User user = userRepository.findByUsername(userSignInDto.getUsername());
        Duration jwtExpirationDuration = Duration.ofHours(1); // 예시로 1시간 설정
        // 토큰 생성
        String token = tokenProvider.generateToken(user, jwtExpirationDuration);

        UserSignInResponseDto userSignInResponseDto = UserSignInResponseDto.builder()
                .token(token)
                .username(userSignInDto.getUsername())
                .id(user.getId())
                .build();


        return userSignInResponseDto;
    }

    //정보수정
    public void updateUserInformation(String username, String newEmail, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setEmail(newEmail);
            user.setPassword(bCryptPasswordEncoder.encode(newPassword)); // 패스워드를 저장할 때는 암호화 필요
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    //회원 탈퇴
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다: " + username);
        }
    }

//    public HashMap<String, Object> useridOverlap(Long id) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("result", userRepository.existsById(id));
//        return map;
//    }

    //닉네임 중복 검사
    public HashMap<String, Object> nicknameOverlap(String username) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", userRepository.existsByUsername(username));
        return map;
    }
}
