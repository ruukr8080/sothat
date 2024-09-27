package com.ex.sothat.techStack.controller;

import com.ex.sothat.global.common.response.SuccessResponseDto;
import com.ex.sothat.techStack.dto.response.TechStackListReadResponseDto;
import com.ex.sothat.techStack.service.TechStackService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/techStack")
@RequiredArgsConstructor
public class TechStackController {
    
    private final TechStackService teckStackService;

    private static final String READ_TECHSTACK_LIST = "";

    @GetMapping(READ_TECHSTACK_LIST)
    @Operation(summary = "기술스택 리스트 조회")
    public ResponseEntity<SuccessResponseDto<List<TechStackListReadResponseDto>>> readTechStackList(@RequestParam(value = "text", required = false) String text){
        log.info("/techStack/controller : 기술스택 리스트 조회");
        return ResponseEntity.ok().body(teckStackService.readTechStackList(text));
    }
}
