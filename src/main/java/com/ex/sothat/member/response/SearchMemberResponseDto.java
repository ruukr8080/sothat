package com.ex.sothat.member.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchMemberResponseDto {
    private String nickname;
    private String memberId;
}
