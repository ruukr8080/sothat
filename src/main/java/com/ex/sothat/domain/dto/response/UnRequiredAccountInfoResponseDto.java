package com.ex.sothat.domain.dto.response;

import com.ex.sothat.domain.dto.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class UnRequiredAccountInfoResponseDto extends ResponseDto {

    List<String> requiredUserInfoList;

    public UnRequiredAccountInfoResponseDto(List<String> requiredList) {
        super();
        this.requiredUserInfoList = requiredList;
    }

    public static ResponseEntity<ResponseDto> success(List<String> requiredList) {
        ResponseDto responseBody = new UnRequiredAccountInfoResponseDto(requiredList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
