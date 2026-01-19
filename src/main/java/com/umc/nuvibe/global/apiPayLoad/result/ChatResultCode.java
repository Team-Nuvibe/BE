package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatResultCode implements ResultCode{

    CHAT_TIMELINE_SUCCESS(HttpStatus.OK, "CHAT001", "트라이브 챗 타임라인 조회 성공"),
    CHAT_GRID_SUCCESS(HttpStatus.OK, "CHAT002", "트라이브 챗 이미지 그리드 조회 성공"),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
