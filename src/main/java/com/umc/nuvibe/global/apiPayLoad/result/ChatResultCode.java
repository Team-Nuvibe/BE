package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatResultCode implements ResultCode{

    CHAT_TIMELINE_SUCCESS(HttpStatus.OK, "CHAT001", "트라이브 챗 타임라인 조회 성공"),
    CHAT_GRID_SUCCESS(HttpStatus.OK, "CHAT002", "트라이브 챗 이미지 그리드 조회 성공"),
    CHAT_DETAIL_SUCCESS(HttpStatus.OK, "CHAT003", "채팅 이미지 상세 조회 성공"),
    CHAT_SEND_SUCCESS(HttpStatus.OK, "CHAT004", "채팅 발신 성공"),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
