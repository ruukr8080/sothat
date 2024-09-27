package com.ex.sothat.global.common.exception;

import com.ex.sothat.global.common.response.FailResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoThatCodeException.class)
    public ResponseEntity<FailResponseDto> handleNotFoundException(SoThatCodeException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new FailResponseDto(false, ex.getMessage(), ex.getStatus()));
    }
}
