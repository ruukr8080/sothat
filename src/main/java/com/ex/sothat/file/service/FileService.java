package com.ex.sothat.file.service;

import com.ex.sothat.file.dto.response.UploadFileResponseDto;
import com.ex.sothat.global.common.response.SuccessResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public SuccessResponseDto<UploadFileResponseDto> fileUpload(MultipartFile multipartFile);

    public void deleteImages(List<String> deletedImg);
}
