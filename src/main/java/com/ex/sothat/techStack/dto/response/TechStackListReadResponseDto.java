package com.ex.sothat.techStack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechStackListReadResponseDto {
    private String techStack;
    private String iconUrl;
}
