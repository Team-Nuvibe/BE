package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MailErrorCode implements ErrorCode {

    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"MAIL001","유효한 토큰이 아닙니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,"MAIL002","만료된 토큰입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST,"MAIL003","인증되지 않은 이메일입니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"MAIL004","이메일 발송에 실패했습니다."),
    NO_SHA_256(HttpStatus.BAD_REQUEST, "MAIL005","sha-256 알고리즘이 없습니다"),

    ;




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
