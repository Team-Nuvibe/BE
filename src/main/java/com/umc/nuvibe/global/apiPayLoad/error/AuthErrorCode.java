package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"AUTH001", "만료된 jwt 토큰입니다"),
    JWT_INVALID_TOKEN(HttpStatus.BAD_REQUEST,"AUTH002","유효하지 않은 jwt 토큰입니다"),
    JWT_GENERATED_FAILED(HttpStatus.BAD_REQUEST,"AUTH003", "jwt 토큰 생성 실패")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
