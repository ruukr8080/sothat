package com.ex.sothat.member.request;

import com.ex.sothat.member.Role;
import lombok.Builder;
import lombok.Data;

/**
 * 사용자가 입력 할 수 있는 데이터
 * 이메일
 * 비밀번호
 * 닉네임
 */
@Data
@Builder
public class MemberSaveRequestDto {
    String loginId;
    String memberEmail;
    String memberNickname;
    String memberName;
    String loginType;
    Role role;
}
