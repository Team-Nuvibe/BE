package com.umc.nuvibe.domain.tribe.code;

import com.umc.nuvibe.global.apiPayLoad.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserTribeResultCode implements ResultCode {

    GET_TRIBE_SUCCESS(HttpStatus.OK, "USERTRIBE2001", "트라이브 챗 목록 조회 성공"),
    USERTRIBE_LEAVE_SUCCESS(HttpStatus.OK, "USERTRIBE2002", "트라이브 챗 퇴장 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
