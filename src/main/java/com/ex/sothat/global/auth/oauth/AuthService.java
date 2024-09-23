package com.ex.sothat.global.auth.oauth;

import com.ex.sothat.domain.dto.*;
import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.global.common.Authority;
import com.ex.sothat.domain.dao.RefreshToken;
import com.ex.sothat.global.auth.jwt.JwtTokenProvider;
import com.ex.sothat.domain.dao.repository.AccountRepository;
import com.ex.sothat.domain.dao.repository.RefreshTokenRepository;
import com.ex.sothat.global.common.OAuthAttributes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


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
        account.addRole(Authority.ROLE_USER); // 아직 Authority 설정이 없음 회원가입할 때 기본 권한(ROLE_USER)
        accountRepository.save(account);
        return account.getName() + "님 회원가입을 환영합니다";

    }

    @Transactional
    public TokenRequest login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

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
        if (!jwtTokenProvider.validateToken(reissueRequest.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(reissueRequest.getRefreshToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if (!refreshToken.getValue().equals(reissueRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenRequest tokenRequest = jwtTokenProvider.createToken(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenRequest.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenRequest;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.debug("OAuth2 로그인 요청: {}", userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.debug("OAuth2 속성: {}", attributes);

        AccountRequest accountRequest = OAuthAttributes.extract(registrationId, attributes);
        accountRequest.setProvider(registrationId);

        Account account = updateOrSaveUser(accountRequest);

        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        Authentication auth = new UsernamePasswordAuthenticationToken(account.getEmail(), "", authorities);
        TokenRequest tokenRequest = jwtTokenProvider.createToken(auth);

        Map<String, Object> customAttribute = attributes;
        customAttribute.put("token", tokenRequest.getAccessToken());
        log.debug("사용자 권한: {}", account.getRoles());

        return new DefaultOAuth2User(authorities, customAttribute, userNameAttributeName);
    }

    @Transactional
    public Account updateOrSaveUser(AccountRequest accountRequest) {
        Account account = accountRepository
                .findUserByEmailAndProvider(accountRequest.getEmail(), accountRequest.getProvider())
                .orElse(null);

        if (account == null) {
            account = accountRequest.toEntity();
            account.addRole(Authority.ROLE_USER);  // 새 사용자에게 기본 권한 부여
            log.debug("유저 생성 : {}", accountRequest.getEmail());
        } else {
            log.debug("유저 생성 실패함. 반려된 이메일 : {}", accountRequest.getEmail());
            log.debug("유저 생성 실패함. 반려된 이메일의 권한 : {}", accountRequest.getProvider());
        }

        // 특정 조건에 따라 관리자 권한 부여 (예: 특정 이메일 도메인)
        if (accountRequest.getEmail().endsWith("ruukr8080@gmail.com")) { // ADMIN으로 내 구글 계정 박음
            account.addRole(Authority.ROLE_ADMIN);
            log.debug("ROLE_ADMIN 권한 들어간 계정: {}", accountRequest.getEmail());
        }

        return accountRepository.save(account);
    }
}