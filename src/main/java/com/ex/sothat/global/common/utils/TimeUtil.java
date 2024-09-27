package com.ex.sothat.global.common.utils;


import com.ex.sothat.global.common.exception.GlobalErrorCode;
import com.ex.sothat.global.common.exception.SoThatCodeException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Service
public class TimeUtil {

    public static LocalDateTime refreshExpiredTime = LocalDateTime.now().plusHours(2);

    public static LocalDate getRecruitEnd(String end, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate recruitEnd = LocalDate.parse(end, formatter);

        if(recruitEnd.isBefore(LocalDate.now())){
            throw new SoThatCodeException(GlobalErrorCode.RECRUIT_END_IS_BEFORE_NOW);
        }
        return recruitEnd;
    }
}
