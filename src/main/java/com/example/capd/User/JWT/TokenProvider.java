package com.example.capd.User.JWT;

import com.example.capd.User.domain.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenProvider {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String secretKey;

    public TokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(User user, Duration expiredAt) {
        logger.info("[TokenProvider] 토큰 생성 시작");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId()); // 사용자 ID를 클레임에 추가

        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities), token, authorities);

    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
    //토큰에서 값 추출
    public String getUsername(@RequestBody String token) {
        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
        //secretKey 설정, sub 값 추출
        String info = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();

        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;

    }
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //유효한 토큰인지 확인
    public boolean validateToken(@RequestBody String jwtToken) {
        logger.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());

        } catch (SecurityException | MalformedJwtException e) {
            logger.info("SecurityException | MalformedJwtException");
            return false;
        } catch (ExpiredJwtException e) {
            logger.info("ExpiredJwtException");
            return false;
        } catch (UnsupportedJwtException e) {
            logger.info("UnsupportedJwtException");
            return false;
        } catch (IllegalArgumentException e) {
            logger.info("IllegalArgumentException");
            return false;
        } catch (Exception e) {
            logger.info("{}");
            logger.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
