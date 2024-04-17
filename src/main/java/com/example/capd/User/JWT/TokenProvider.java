//package com.example.capd.User.JWT;
//
//import com.example.capd.User.domain.Authority;
//import com.example.capd.User.domain.User;
//import com.example.capd.User.service.CustomUserDetailsService;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.time.Duration;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class TokenProvider {
//
//    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
//    private  Key secretKey;
//
//    @Value("${security.jwt.token.secret-key}")
//    private String salt;
//    private final long exp = 1000L * 60 * 60;
//
//    private final CustomUserDetailsService userDetailsService;
//
//    @PostConstruct
//    protected void init() {
//        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
//    }
//
//
//
//    // 토큰 생성
//    public String createToken(String userId, List<Authority> roles) {
//        Claims claims = Jwts.claims().setSubject(userId);
//        claims.put("roles", roles);
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + exp))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//
//    // 권한정보 획득
//    // Spring Security 인증과정에서 권한확인을 위한 기능
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//
//    public String getUserId(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("Authorization");
//    }
//
//
//    //토큰에서 값 추출
//    public String getUsername(@RequestBody String token) {
//        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
//        //secretKey 설정, sub 값 추출
//        String info = Jwts.parser().setSigningKey(secretKey)
//                .parseClaimsJws(token).getBody().getSubject();
//
//        logger.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
//        return info;
//
//    }
//
//    //검증
//    public boolean validateToken(String token) {
//        try {
//            // Bearer 검증
//            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
//                return false;
//            } else {
//                token = token.split(" ")[1].trim();
//            }
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            // 만료되었을 시 false
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
