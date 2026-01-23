package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WebsocketErrorCode implements ErrorCode {

    WS_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "WS001", "웹소켓 인증이 필요합니다."),
    WS_SUBSCRIBE_FORBIDDEN(HttpStatus.FORBIDDEN, "WS002", "해당 트라이브 채널을 구독할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
