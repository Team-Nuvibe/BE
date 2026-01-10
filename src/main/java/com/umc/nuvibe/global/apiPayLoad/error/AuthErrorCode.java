package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"AUTH001", "만료된 jwt 토큰입니다."),
    JWT_INVALID_TOKEN(HttpStatus.BAD_REQUEST,"AUTH002","유효하지 않은 jwt 토큰입니다."),
    JWT_GENERATED_FAILED(HttpStatus.BAD_REQUEST,"AUTH003", "jwt 토큰 생성 실패했습니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"AUTH004", "jwt 토큰ㅇ르 찾을 수 없습니다."),
    AUTHORIZATION_HEADER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH005", "autorization header를 찾을 수 없습니다"),
    INVALID_AUTHORIZATION_FORMAT(HttpStatus.BAD_REQUEST, "AUTH006", "유효하지 않은 헤더 포맷입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
