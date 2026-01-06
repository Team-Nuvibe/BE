package com.umc.nuvibe.global.apiPayLoad.exception;

import com.umc.nuvibe.global.apiPayLoad.error.ErrorCode;
import org.springframework.http.HttpStatus;

// 비즈니스상 예외 발생기
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public String getMessage() {
        return errorCode.getMessage();
    }

}
