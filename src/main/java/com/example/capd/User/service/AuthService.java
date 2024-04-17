package com.example.capd.User.service;

import com.example.capd.User.JWT.JwtUtil;
import com.example.capd.User.domain.User;
import com.example.capd.User.dto.CustomUserInfoDto;
import com.example.capd.User.dto.LoginRequestDto;
import com.example.capd.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService{

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    @Transactional
    public String login(LoginRequestDto dto) {
        String userId = dto.getUserId();
        String password = dto.getPassword();
        User user = userRepository.findUserByUserId(userId);
        if(user == null) {
            throw new UsernameNotFoundException("Id가 존재하지 않습니다.");
        }

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);

        String accessToken = jwtUtil.createAccessToken(info);
        return accessToken;
    }
}