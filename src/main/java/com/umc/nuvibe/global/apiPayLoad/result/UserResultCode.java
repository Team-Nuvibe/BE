package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserResultCode implements ResultCode {

    USER_SIGNUP_OK(HttpStatus.CREATED,"USER001", "유저 회원가입 성공"),
    USER_LOGIN_OK(HttpStatus.CREATED,"USER002", "유저 로그인 성공"),
    USER_LOGOUT_OK(HttpStatus.CREATED,"USER003", "유저 로그아웃 성공"),
    USER_WITHDRAW_OK(HttpStatus.CREATED,"USER004", "유저 회원탈퇴 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
