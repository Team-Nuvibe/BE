package com.umc.nuvibe.global.apiPayLoad.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getHttpStatus();
    String getCode();

    String getMessage();

}
