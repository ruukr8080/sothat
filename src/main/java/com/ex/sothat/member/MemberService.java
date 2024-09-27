package com.ex.sothat.member;


import com.ex.sothat.global.common.response.SuccessResponseDto;
import com.ex.sothat.member.response.MemberResponseDto;
import com.ex.sothat.member.response.SearchMemberResponseDto;

import java.util.List;

public interface MemberService {
    void withdrawn(String memberId);
    SuccessResponseDto<MemberResponseDto> readMember(String memberId);
    SuccessResponseDto<List<SearchMemberResponseDto>> searchMember(String text);
}