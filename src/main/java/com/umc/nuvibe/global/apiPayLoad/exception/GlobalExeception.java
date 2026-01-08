package com.umc.nuvibe.global.apiPayLoad.exception;

// 시스템 전역 예외 발생기

import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalExeception extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalExeception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return this.errorCode.getHttpStatus();
    }

    public ErrorCode getReason() {
        return this.errorCode;
    }


}
