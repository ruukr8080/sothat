package com.ex.sothat.service;

import com.ex.sothat.entity.Account;
import com.ex.sothat.entity.Authority;
import com.ex.sothat.dto.OAuthAttributes;
import com.ex.sothat.dto.AccountProfile;
import com.ex.sothat.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("OAuth2 유저 어트리븉 : {}", attributes);

        AccountProfile accountProfile = OAuthAttributes.extract(registrationId, attributes);
        accountProfile.setProvider(registrationId);

        Account account = updateOrSaveUser(accountProfile);

        Map<String, Object> customAttribute = getCustomAttribute(registrationId, userNameAttributeName, attributes, accountProfile);
        log.info("들어온 유저 권한 : {}", account.getRoles());

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

        log.info("잘 넣었나? {}",customAttribute);
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
            log.info("유저 생성 : {}", accountProfile.getEmail());
        } else {
            log.info("유저 생성 빠꾸먹음: {}", accountProfile.getEmail());
        }

        // 특정 조건에 따라 관리자 권한 부여 (예: 특정 이메일 도메인)
        if (accountProfile.getEmail().endsWith("@admin.com")) {
            account.addRole(Authority.ROLE_ADMIN);
            log.info("Admin role added to: {}", accountProfile.getEmail());
        }

        return accountRepository.save(account);
    }
}