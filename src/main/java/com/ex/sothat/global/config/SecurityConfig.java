package com.ex.sothat.global.config;


import com.ex.sothat.domain.app.AccountService;
import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.domain.dao.repository.AccountRepository;
import com.ex.sothat.global.config.jwt.JwtProvider;
import com.ex.sothat.global.config.jwt.JwtTokenValidatorFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final String redirectUrl;
    private final String redirectUrlFailed;

    @Value("${front-server.url}")
    private String frontServerUrl;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontServerUrl)); // 나중에 바꿀 프론트 앱 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry view) {
        view.addViewController("/").setViewName("index");
        view.addViewController("/joinPage").setViewName("joinPage");
        view.addViewController("/loginPage").setViewName("loginPage");
        view.addViewController("/homePage").setViewName("homePage");
    }

    @Autowired
    public SecurityConfig(AccountRepository accountRepository, AccountService accountService, @Value("${jwt.redirect-url}") String redirectUrl, @Value("${jwt.redirect-url-failed}") String redirectUrlFailed, JwtProvider jwtProvider) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.redirectUrl = redirectUrl;
        this.redirectUrlFailed = redirectUrlFailed;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/test/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(auth -> auth.disable())
                .formLogin(auth -> auth.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService())
                        )
                        .successHandler(new CustomAuthenticationSuccessHandler(accountRepository, jwtProvider, redirectUrl))
                        .failureHandler(new CustomAuthenticationFailureHandler(redirectUrlFailed))
                )
                .addFilterBefore(new JwtTokenValidatorFilter(jwtProvider, accountRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            // loadUser 람다
            try {
                OAuth2User oauth2User = delegate.loadUser(userRequest);
                // OAuth 정보를 가져온다.
                String oauth = userRequest.getClientRegistration().getClientName();
                String email = null;
                String nickname = null;
                System.out.println("OAUTH:" + oauth);
                if (oauth.equals("Google")) {
                    email = oauth2User.getAttribute("email");
                    nickname = oauth2User.getAttribute("name");
                }
//                else if (oauth.equals("Kakao")) {
//                    email = (String) ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email");
//                    nickname = (String) ((Map<String, Object>) oauth2User.getAttribute("properties")).get("nickname");
//                }

                Random random = new Random();
                String randomNum = "";
                int attempt = 0;
                final int MAX_ATTEMPTS = 1000;

                while (accountService.isAccountNicknameDuplicated(nickname + randomNum)) {
                    if (attempt >= MAX_ATTEMPTS) {
                        throw new RuntimeException("Unable to generate unique nickname after " + MAX_ATTEMPTS + " attempts.");
                    }
                    randomNum = String.valueOf(random.nextInt(1000)); // 0 ~ 999 사이의 숫자를 추가
                    attempt++;
                }
                nickname += randomNum;

                Optional<Account> account = accountRepository.findByEmail(email);
                log.info(" if (!account.isPresent()) 통과. {}", email);
                if (!account.isPresent()) {
                    log.info(" if (!account.isPresent()) 통과. {}", email);
                    Account newUser = new Account();
                    newUser.setEmail(email);
                    newUser.setNickname(nickname);
                    newUser.setOauthType(oauth);
                    newUser.setSignupDate(LocalDateTime.now());
                    newUser.setUserRole("ROLE_USER");
                    accountRepository.save(newUser);
                } else if (account.get().getOauthType().equals(oauth)) {
                    //해당 이메일로 가입된 기존 유저와 같은 oauth로 로그인한 경우
                    log.info("{} 해당 이메일로 가입된 기존 유저와 같은 oauth로 로그인한 경우", oauth);
                    if (account.get().getBannedDate() != null) {
//                        //해당 이메일로 가입이 되었지만 탈퇴 혹은 벤 먹은 경우
                        log.info("{} 해당 이메일로 가입이 되었지만 탈퇴 혹은 벤 먹은 경우", account.get().getEmail());
                        // newNewUser 변수로 지정하여 newNewUser
                        Account newNewUser = account.get();
                        accountService.recoverData(newNewUser);
                        accountRepository.save(newNewUser);
                        //탈퇴한 유저 재 가입 시킴
                    }
                    // 특이 사항 없는 유저면 걍 가입 로직에 태워서 accountRepository.save().
                }
                // 해당 이메일로 가입된 유저가 있지만 현재 OAuth타입과 다른 경우. 이거 present 함수로 바꿀수도?
                else if (!account.get().getOauthType().equals(oauth)) {
                    log.info("가입한 이메일은 있는데 현재 oauth가 다름, present 함수로 바꿔서 테스트 해보자");
                }
                return oauth2User;
            } catch (Exception e) {
                throw new OAuth2AuthenticationException(e.getMessage());
            }
        };
    }
}

class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;
    private final String redirectUrl;

    @Value("${jwt.cookie-secure}")
    private boolean cookieSecure; // 배포하면 못쓰는 쿠키 키

    public CustomAuthenticationSuccessHandler(AccountRepository accountRepository, JwtProvider jwtProvider, String redirectUrl) {
        this.accountRepository = accountRepository;
        this.jwtProvider = jwtProvider;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        if (email == null) {
            email = (String) ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email");
        }
        Account account = accountRepository.findUserByEmailAndBannedDateIsNull(email).orElseThrow();
        String jwt = jwtProvider.generateToken(account, 60 * 60 * 1000L);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //TODO 배포 시 true로 변경해야함 (TLS 적용 시)
        cookie.setSecure(cookieSecure);
        response.addCookie(cookie);

        response.sendRedirect(redirectUrl);
    }
}

@Slf4j
class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String redirectUrlFailed;

    public CustomAuthenticationFailureHandler(String redirectUrlFailed) {
        this.redirectUrlFailed = redirectUrlFailed;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("OAuth2 authentication failed.");
        exception.printStackTrace();

        response.sendRedirect(redirectUrlFailed);
    }
}
