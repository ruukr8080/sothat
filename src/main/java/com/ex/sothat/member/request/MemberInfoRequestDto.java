package com.ex.sothat.member.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class MemberInfoRequestDto {
    private String memberId;
    private String category;
}
