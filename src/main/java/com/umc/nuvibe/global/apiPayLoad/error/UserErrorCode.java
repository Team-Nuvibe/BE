package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"MEMBER001","사용자를 찾을 수 없습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
