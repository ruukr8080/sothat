package com.ex.sothat.service;

import com.ex.sothat.domain.dto.LoginRequest;
import com.ex.sothat.domain.dto.ReissueRequest;
import com.ex.sothat.domain.dto.SignupRequest;
import com.ex.sothat.domain.dto.TokenRequest;
import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.global.Authority;
import com.ex.sothat.domain.dao.RefreshToken;
import com.ex.sothat.global.jwt.JwtTokenProvider;
import com.ex.sothat.domain.dao.repository.AccountRepository;
import com.ex.sothat.domain.dao.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @Transactional
    public String signup(SignupRequest request) {
        if (accountRepository.existsByEmail(request.getEmail()
        )) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        } else if (accountRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        Account account = Account.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .provider("local")
                .build();
        account.addRole(Authority.ROLE_USER); // 아직 Authority 설정이 없음 회원가입할 때 기본 권한(ROLE_USER) 정도는 줘야 할 듯
        accountRepository.save(account);
        return account.getName() + "님 회원가입을 환영합니다";

    }
    @Transactional
    public TokenRequest login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다."));

        if (!encoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenRequest tokenRequest = jwtTokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenRequest.getRefreshToken())
                .build();


        refreshTokenRepository.save(refreshToken);

        return tokenRequest;

    }

    @Transactional
    public TokenRequest reissue(ReissueRequest reissueRequest) {

        log.info("재발행 요청 {}", reissueRequest);
        if(!jwtTokenProvider.validateToken(reissueRequest.getRefreshToken())){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequest.getRefreshToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자 입니다."));

        if (!refreshToken.getValue().equals(reissueRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenRequest tokenRequest = jwtTokenProvider.createToken(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenRequest.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);


        return tokenRequest;
    }
}