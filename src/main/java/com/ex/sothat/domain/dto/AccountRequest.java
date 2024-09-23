package com.ex.sothat.domain.dto;

import com.ex.sothat.domain.dao.Account;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRequest {
    private String email; // 사용자의 이메일 -> 계정 id가 될거임
    private String name; // 사용자 닉네임으로 쓸거
    private String provider; // 로그인서비스 뭐 썻는지. 현재 구글밖에 없음

    // DTO로 Entity를 생성할 때.
    public Account toEntity() {
        return Account.builder()
                .email(this.email)
                .name(this.name)
                .provider(this.provider)
                .build();
    }
}
