package com.ex.sothat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueRequest {
    private String accessToken;
    private String refreshToken;
}