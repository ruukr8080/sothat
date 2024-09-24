package com.ex.sothat.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {
    private Long accountId;

    private String email;
    private LocalDate birthdate;
    private String nickname;
    private String oauthType;
    private LocalDateTime signupDate;
    private String profileS3Key;

//    private LocalDateTime firstLoginDate; //사용자 이용 분석,통계할 때 쓸거
//    private LocalDateTime lastLoginDate; // 사용자 이용 분석,통계할 때 쓸
}
