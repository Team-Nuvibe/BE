package com.umc.nuvibe.global.apiPayLoad.result;

import org.springframework.http.HttpStatus;

public interface ResultCode {

    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
