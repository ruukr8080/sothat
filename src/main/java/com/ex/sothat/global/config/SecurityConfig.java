package com.ex.sothat.global.config;

import com.ex.sothat.domain.app.JwtSecurityHandler;
import com.ex.sothat.global.auth.jwt.JwtFilter;
import com.ex.sothat.global.auth.oauth.AuthService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final JwtSecurityHandler jwtAuthenticationEntryPoint;
    private final JwtSecurityHandler jwtAccessDeniedHandler;
    private final AuthService authService;
    private final JwtFilter jwtFilter;
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

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(customizer -> {
                    customizer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                    customizer.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginPage")
                        .defaultSuccessUrl("/articles"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/", "/index", "/static/**", "/css/**", "/js/**", "/joinPage", "/loginPage","/homePage", "/api/auth/**").permitAll()
                        .requestMatchers("/img/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/visit/**", "/homePage/**","/login/**",  "/oauth2/**", "/login/oauth2/code/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginPage")  // "/joinPage"에서 "/loginPage"로 변경
                        .userInfoEndpoint(userInfo -> userInfo.userService(authService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String token = (String) oAuth2User.getAttribute("token");

                            // 쿠키에 JWT 박은거 확인할라면 크롬에서 application에서 Cookie
                            Cookie cookie = new Cookie("jwt", token);
                            cookie.setPath("/");
                            cookie.setHttpOnly(true);
                            cookie.setMaxAge(3600); //초단위만 박을 수 있나?
                            response.addCookie(cookie);

                            response.sendRedirect("/homePage"); // 로그인 성공 후 리다이렉트 이것도 위에 requestmatcher에 넣어줘야되는거같음
                        }))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
