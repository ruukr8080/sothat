package com.ex.sothat.config;

import com.ex.sothat.domain.auth.jwt.JWTFilter;
import com.ex.sothat.domain.auth.jwt.JWTService;
import com.ex.sothat.domain.auth.jwt.JWTUtil;
import com.ex.sothat.domain.auth.jwt.RefreshTokenRepository;
import com.ex.sothat.domain.auth.oauth2.CustomClientRegistrationRepo;
import com.ex.sothat.domain.auth.oauth2.LoginSuccessHandler;
import com.ex.sothat.domain.auth.oauth2.LogoutSuccessHandler;
import com.ex.sothat.domain.auth.oauth2.service.CustomOauth2Service;
import com.ex.sothat.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@Component
@Slf4j
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomOauth2Service customOauth2Service;
    private final JWTUtil jwtUtil;
    private final JWTService jwtService;
    private final RefreshTokenRepository refresh;
    private final CustomClientRegistrationRepo customClientRegistrationRepo;

    /**
     * ../config/Paths.java
     * StaticResourceLocation
     */
    @Override
    public void addViewControllers(@NotNull ViewControllerRegistry registry) {
        Paths.configureViewControllers(registry);
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Paths.COMMON_URLS).permitAll()
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/member/**").hasRole(Role.MEMBER.name())
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 없어도되긴함
                        .anyRequest().authenticated())
                .logout(logout -> logout.addLogoutHandler(new LogoutSuccessHandler(refresh)))
                .oauth2Login(login -> login
                        .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
                        .userInfoEndpoint(endPoint -> endPoint.userService(customOauth2Service))
                        .successHandler(new LoginSuccessHandler(jwtUtil, jwtService)))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
