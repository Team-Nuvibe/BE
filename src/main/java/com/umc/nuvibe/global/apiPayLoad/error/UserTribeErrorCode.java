package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserTribeErrorCode implements ErrorCode {

    USERTRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "USERTRIBE001", "해당 트라이브 챗을 찾을 수 없습니다."),
    USERTRIBE_NOT_JOINED(HttpStatus.BAD_REQUEST, "USERTRIBE001", "해당 트라이브 챗에 입장해있지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
