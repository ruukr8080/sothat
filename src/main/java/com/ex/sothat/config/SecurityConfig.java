package com.ex.sothat.config;

import com.ex.sothat.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final OAuth2Service oAuth2Service;

//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("index");
//        registry.addViewController("/join").setViewName("join");
//        registry.addViewController("/login").setViewName("login");
//    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 설정 사용 X
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")
                )
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 사용 X
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/home", "/join/**","/login/**", "/oauth2/**", "/css/**", "/js/**","thymeleaf").permitAll()
                        .requestMatchers("/loginPage").permitAll() //여기에도 해줘야 되는구나
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/settings/**").permitAll()
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/oauth2/authorization/google").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginPage") //oauth2Login 여기 필터에 인증 권한 안줘서 auth에도 따로 설정을 해줘야만 넘어감. 그냥 로그인 해선 안넘억마.
                        .defaultSuccessUrl("/oauth/loginInfo", true) // 로그인 성공 시 이동할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service) // 사용자가 로그인에 성공하면 oAuth2Service 서비스 로직 타게함.
                        )
                );
                return http.build();
    }
}
