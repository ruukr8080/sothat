package com.ex.sothat.global.common;

import com.ex.sothat.domain.dto.AccountRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google", (attribute) -> {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setName((String)attribute.get("name"));
        accountRequest.setEmail((String)attribute.get("email"));

        return accountRequest;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, AccountRequest> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, AccountRequest> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static AccountRequest extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
