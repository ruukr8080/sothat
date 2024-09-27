package com.ex.sothat.member.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 사용자가 수정할 수 있는 데이터
 * 비밀번호
 * 닉네임
 * 자기소개
 * 프로필*/
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequestDto {
    private String memberNickname;
    private String introduce;
    private String profile;
}
