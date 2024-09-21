package com.ex.sothat.config;

import com.ex.sothat.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final OAuth2Service oAuth2Service;
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
//    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 설정 사용 X
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/oauth/loginInfo", true) // 로그인 성공 시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service) // 사용자가 로그인에 성공하면 oAuth2Service 서비스 로직 타게함.
                        )
                );
                return http.build();
    }
}
