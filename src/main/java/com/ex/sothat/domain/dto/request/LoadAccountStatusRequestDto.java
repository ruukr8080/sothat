package com.ex.sothat.domain.dto.request;


import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoadAccountStatusRequestDto {

    @Nullable
    private Long accountId;
}