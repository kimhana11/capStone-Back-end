package com.example.capd.User.service;

import com.example.capd.User.domain.User;
import com.example.capd.User.JWT.TokenProvider;
import com.example.capd.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;


    public String createNewAccessToken(String refreshToken){
        // 토큰 유효성 검사에 실패하면 예외
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new IllegalArgumentException("Unexpected token"));

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
