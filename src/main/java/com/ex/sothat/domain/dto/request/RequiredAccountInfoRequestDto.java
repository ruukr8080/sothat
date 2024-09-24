package com.ex.sothat.domain.dto.request;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RequiredAccountInfoRequestDto {

    @Nullable
    private Character gender;
    @Nullable
    private LocalDate birthdate;
    @Nullable
    private String nickname;
    @Nullable
    private String profileS3Key;
}
