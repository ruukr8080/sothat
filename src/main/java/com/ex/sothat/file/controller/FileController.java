package com.ex.sothat.file.controller;

import com.ex.sothat.file.dto.response.UploadFileResponseDto;
import com.ex.sothat.file.service.FileService;
import com.ex.sothat.global.common.response.SuccessResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    
    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 업로드", description = "이미지 width가 400보다 크면 resize(width:400 비율고정)해서 저장")
    public ResponseEntity<SuccessResponseDto<UploadFileResponseDto>> fileUpload(@RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        log.info("/file/controller : 이미지 업로드");
        return ResponseEntity.ok().body(fileService.fileUpload(file));
    }

    
    
}
