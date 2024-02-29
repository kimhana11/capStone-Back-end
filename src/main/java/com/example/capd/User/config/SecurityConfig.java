package com.example.capd.User.config;

import com.example.capd.User.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toString())
                .requestMatchers("/static/**");
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults()) // CORS 활성화
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("**/login", "**/signup","/user/signup","/user/signup/**", "/user/**", "/user/idCheck").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }




    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception{
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService((UserDetailsService) userService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //,"/user/idCheck/**","**/idCheck/"
//        return http
//                .authorizeHttpRequests()
//                .requestMatchers("**/login", "**/signup","/user/signup","/user/signup/**", "/user/**", "/user/idCheck").permitAll()
//                .anyRequest().authenticated()
//             //   .and()
//             //           .formLogin() // 폼 기반 로그인 설정
//             //           .loginPage("/login")
//             //           .defaultSuccessUrl("/home")
//             //           .and()
//             //           .logout() // 로그아웃 설정
//              //          .logoutSuccessUrl("/login")
//              //          .invalidateHttpSession(true)
//                .and()
//                .csrf().disable() //csrf 비활성화
//                .cors().disable()
//                .build();
//    }
