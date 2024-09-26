package com.ex.sothat.domain.app;

import com.amazonaws.HttpMethod;

public interface S3Service {

    String generatePresignedUrl(String fileName, HttpMethod method);
    void deleteImageFromS3(String fileName);
}
