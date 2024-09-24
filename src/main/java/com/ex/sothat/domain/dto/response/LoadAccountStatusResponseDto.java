package com.ex.sothat.domain.dto.response;


import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.domain.dto.AccountDto;
import com.ex.sothat.domain.dto.ResponseDto;
import com.ex.sothat.global.common.ResponseCode;
import com.ex.sothat.global.common.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadAccountStatusResponseDto extends ResponseDto {

    private AccountDto dto;

    public LoadAccountStatusResponseDto(Account user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.dto = Account.mapToDTO(user);
    }
    public static ResponseEntity<ResponseDto> success(Account user) {
        ResponseDto responseBody = new LoadAccountStatusResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    public static ResponseEntity<ResponseDto> userBane() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.USER_DEACTIVATED, ResponseMessage.USER_DEACTIVATED);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

}
