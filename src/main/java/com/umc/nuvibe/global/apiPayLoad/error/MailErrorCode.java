package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MailErrorCode implements ErrorCode {

    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST,"MAIL003","인증되지 않은 이메일입니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"MAIL004","이메일 발송에 실패했습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST,"MAIL006","유효하지 않은 인증 코드입니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"MAIL007","만료된 인증 코드입니다."),
    CODE_NOT_VERIFIED(HttpStatus.BAD_REQUEST,"MAIL008","인증되지 않은 코드입니다."),
    INVALID_VERIFICATION_TYPE(HttpStatus.BAD_REQUEST,"MAIL009","유효하지 않은 인증 타입입니다."),

    ;




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
