package com.ex.sothat.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberJwtRequestDto {
    String memberEmail;
    String memberName;
    String loginType;
}
