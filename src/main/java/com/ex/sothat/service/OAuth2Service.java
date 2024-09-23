package com.ex.sothat.service;

import com.ex.sothat.domain.dto.TokenRequest;
import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.global.Authority;
import com.ex.sothat.domain.dto.OAuthAttributes;
import com.ex.sothat.domain.dto.AccountProfile;
import com.ex.sothat.global.jwt.JwtTokenProvider;
import com.ex.sothat.domain.dao.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.debug("OAuth2 로그인 들온거(지금은 구글밖에없음): {}", userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.debug("OAuth2 속성 : {}", attributes);

        AccountProfile accountProfile = OAuthAttributes.extract(registrationId, attributes);
        accountProfile.setProvider(registrationId);

        Account account = updateOrSaveUser(accountProfile);
        // JWT 토큰 생성
//        String token = jwtTokenProvider.createToken(account.getEmail(), account.getRoles());
        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        Authentication auth = new UsernamePasswordAuthenticationToken(account.getEmail(), "", authorities);
        TokenRequest tokenRequest = jwtTokenProvider.createToken(auth);
        Map<String, Object> customAttribute = getCustomAttribute(registrationId, userNameAttributeName, attributes, accountProfile);
        customAttribute.put("token", tokenRequest.getAccessToken());  // JWT 토큰 추가
        log.debug("들어온 유저 권한 값 : {}", account.getRoles());
        return new DefaultOAuth2User(
                account.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList()),
                customAttribute,
                userNameAttributeName);

    }

    public Map<String, Object> getCustomAttribute(String registrationId,
                                                  String userNameAttributeName,
                                                  Map<String, Object> attributes,
                                                  AccountProfile accountProfile) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", accountProfile.getName());
        customAttribute.put("email", accountProfile.getEmail());

        log.debug("잘 넣었나? {}",customAttribute);
        return customAttribute;
    }

    @Transactional
    public Account updateOrSaveUser(AccountProfile accountProfile) {
        Account account = accountRepository
                .findUserByEmailAndProvider(accountProfile.getEmail(), accountProfile.getProvider())
                .orElse(null);

        if (account == null) {
            account = accountProfile.toEntity();
            account.addRole(Authority.ROLE_USER);  // 새 사용자에게 기본 권한 부여
            log.debug("유저 생성 : {}", accountProfile.getEmail());
        } else {
            log.debug("유저 생성 실패함. 반려된 이메일 : {}", accountProfile.getEmail());
            log.debug("유저 생성 실패함. 반려된 이메일의 권한 : {}", accountProfile.getProvider());
        }

        // 특정 조건에 따라 관리자 권한 부여 (예: 특정 이메일 도메인)
        if (accountProfile.getEmail().endsWith("ruukr8080@gmail.com")) { // ADMIN으로 내 구글 계정 박음
            account.addRole(Authority.ROLE_ADMIN);
            log.debug("ROLE_ADMIN 권한 들어간 계정: {}", accountProfile.getEmail());
        }

        return accountRepository.save(account);
    }
}