package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserTribeResultCode implements ResultCode {

    GET_USERTRIBE_SUCCESS(HttpStatus.OK, "USERTRIBE001", "트라이브 챗 목록 조회 성공"),
    USERTRIBE_LEAVE_SUCCESS(HttpStatus.OK, "USERTRIBE002", "트라이브 챗 퇴장 성공"),
    USERTRIBE_ACTIVATE_SUCCESS(HttpStatus.OK, "USERTRIBE003", "트라이브 챗 활성화 성공"),
    USERTRIBE_FAVORITE_SUCCESS(HttpStatus.OK, "USERTRIBE004", "트라이브 챗 즐겨찾기 성공"),
    USERTRIBE_READ_SUCCESS(HttpStatus.OK, "USERTRIBE005", "트라이브 챗 읽음 처리 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
