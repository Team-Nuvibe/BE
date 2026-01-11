package com.umc.nuvibe.domain.tribe.code;

import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TribeErrorCode implements ErrorCode {

    TRIBE_NOT_FOUND(HttpStatus.NOT_FOUND, "TRIBE4041", "해당 트라이브를 찾을 수 없습니다."),

    ALREADY_JOINED(HttpStatus.BAD_REQUEST, "TRIBE4001", "이미 해당 태그의 트라이브에 가입되어 있습니다."),
    ACTIVATION_NOT_READY(HttpStatus.BAD_REQUEST, "TRIBE4002", "최소 5명이 모여야 활성화할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
