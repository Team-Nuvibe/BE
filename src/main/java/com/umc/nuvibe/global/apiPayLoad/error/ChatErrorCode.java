package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT001", "해당 채팅을 찾을 수 없습니다."),
    CHAT_CURSOR_INVALID(HttpStatus.BAD_REQUEST, "CHAT002", "유효하지 않은 채팅 커서입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
