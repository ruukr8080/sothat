package com.ex.sothat.dto;

import com.ex.sothat.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserProfile {
    private String name; // 사용자 이름
    private String provider; // 로그인한 서비스
    private String email; // 사용자의 이메일

    // DTO 파일을 통하여 Entity를 생성.
    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .provider(this.provider)
                .build();
    }
}
