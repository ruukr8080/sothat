package com.ex.sothat.domain.dto.response;

import com.ex.sothat.domain.dto.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
public class DuplicateNicknameResponseDto extends ResponseDto {

    private boolean dupleNick;

    public DuplicateNicknameResponseDto(boolean dupleNick) {
        super(); //메세지 랑 코드는 알아서 드감.
        this.dupleNick = dupleNick;
    }

    public static ResponseEntity<ResponseDto> success(boolean dupleNick) {
        ResponseDto responseBody = new DuplicateNicknameResponseDto(dupleNick);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
