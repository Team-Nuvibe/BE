package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TribeErrorCode implements ErrorCode {

    TRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRIBE001", "해당 트라이브를 찾을 수 없습니다."),

    ALREADY_JOINED(HttpStatus.BAD_REQUEST, "TRIBE002", "이미 해당 태그의 트라이브에 가입되어 있습니다."),
    ACTIVATION_NOT_READY(HttpStatus.BAD_REQUEST, "TRIBE003", "최소 5명이 모여야 활성화할 수 있습니다."),
    ALREADY_CREATED_VERSION(HttpStatus.BAD_REQUEST, "TRIBE004", "이미 동일한 버전의 트라이브가 존재합니다."),
    TRIBE_FULL_RETRY(HttpStatus.BAD_REQUEST, "TRIBE005", "트라이브 챗 입장 수 제한을 초과하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
