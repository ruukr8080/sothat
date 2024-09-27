package com.ex.sothat.global.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SoThatCodeException extends RuntimeException{
    private final String message;
    private final int status;

    public SoThatCodeException(GlobalErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

    public SoThatCodeException(String message, int status){
        this.message = message;
        this.status = status;
    }
}
