package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecapErrorCode implements ErrorCode {

    PERIOD_NOTFOUND(HttpStatus.NOT_FOUND, "RECAP_ERR001", "기간을 다시 입력해주세요"),
    NOT_ENOUGH_DATA(HttpStatus.NOT_FOUND, "RECAP_ERR002", "업로드 된 이미지가 없습니다. ")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
