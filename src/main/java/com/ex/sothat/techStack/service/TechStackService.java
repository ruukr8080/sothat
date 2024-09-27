package com.ex.sothat.techStack.service;


import com.ex.sothat.global.common.response.SuccessResponseDto;
import com.ex.sothat.techStack.dto.response.TechStackListReadResponseDto;

import java.util.List;

public interface TechStackService {
    public SuccessResponseDto<List<TechStackListReadResponseDto>> readTechStackList(String text);
}
