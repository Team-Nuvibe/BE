package com.umc.nuvibe.domain.tribe.code;

import com.umc.nuvibe.global.apiPayLoad.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TribeResultCode implements ResultCode {

    TRIBE_JOIN_SUCCESS(HttpStatus.OK, "TRIBE2001", "트라이브 입장 성공"),
    TRIBE_ACTIVATE_SUCCESS(HttpStatus.OK, "TRIBE2002", "트라이브 활성화 성공"),

    TRIBE_CREATE_SUCCESS(HttpStatus.CREATED, "TRIBE2011", "새로운 버전의 트라이브 생성 및 입장 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
