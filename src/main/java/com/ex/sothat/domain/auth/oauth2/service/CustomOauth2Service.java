package com.ex.sothat.domain.auth.oauth2.service;

import com.ex.sothat.domain.auth.oauth2.dto.*;
import com.ex.sothat.member.Member;
import com.ex.sothat.member.MemberRepository;
import com.ex.sothat.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        System.out.println("들어온 유저 정보 함 봐보자ㅏㅏㅏㅏㅏㅏㅏ"+oAuth2User);
        log.info("로그인포로 또보자 {}", oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        log.info("로그인포 리스폰스 전 {}", oAuth2Response);
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }
        String providerInfo = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Optional<Member> existData = memberRepository.findByProvider(providerInfo);
        log.info("로그인포 리스폰스 후{}", oAuth2Response);
        log.info("로그인포 existData {}", existData);
        log.info("로그인포 providerInfo {}", providerInfo);
        if (existData.isEmpty()) {

            String memberId = UUID.randomUUID().toString().substring(0,8);
            while(memberRepository.existsById(memberId)){
                memberId = UUID.randomUUID().toString().substring(0,8);
            }
            log.info("로그인포 memberId save 하기 전에  {}", memberId);

            Member member = Member.builder()
                    .id(memberId)
                    .provider(providerInfo)
                    .memberEmail(oAuth2Response.getEmail())
                    .memberNickname(oAuth2Response.getName())
                    .role(Role.INCOMPLETE_MEMBER)
                    .build();
            log.info("로그인포 member 빌더만 거쳐보자  {}", member);
            memberRepository.save(member);
            log.info("로그인포 member save 후 거쳐보자  {}", member);
            return new CustomOAuth2User(member);
        }
        else {

            Member member = existData.get();

            log.info("로그인포 memver 그냥 저장하는거  {}", member);
            return new CustomOAuth2User(member);

        }
    }
}
