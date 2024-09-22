package com.ex.sothat.service;

import com.ex.sothat.dto.LoginRequest;
import com.ex.sothat.dto.ReissueRequest;
import com.ex.sothat.dto.SignupRequest;
import com.ex.sothat.jwt.JwtTokenProvider;
import com.ex.sothat.repository.MemberRepository;
import com.ex.sothat.repository.RefreshTokenRepository;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @Transactional
    public String signup(SignupRequest request) {
        if (memberRepository.existsByUserid(request.getUserid()
        )) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        } else if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        } else if (memberRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        Account account = Account.builder()
                .userid(request.getUserid())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .authority(Authority.ROLE_ADMIN)
                .build();
        memberRepository.save(account);
        return account.getUserid() + "님 회원가입을 환영합니다";

    }

    @Transactional
    public TokenRequest login(LoginRequest request) {
        Account account = memberRepository.findByUserid(request.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUserid(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenRequest tokenRequest = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenRequest.getRefreshToken())
                .build();


        refreshTokenRepository.save(refreshToken);

        return tokenRequest;

    }

    @Transactional
    public TokenRequest reissue(ReissueRequest reissueRequest) {

        if(!jwtTokenProvider.validateToken(reissueRequest.getRefreshToken())){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequest.getRefreshToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자 입니다."));

        if (!refreshToken.getValue().equals(reissueRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenRequest tokenRequest = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenRequest.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);


        return tokenRequest;
    }
}