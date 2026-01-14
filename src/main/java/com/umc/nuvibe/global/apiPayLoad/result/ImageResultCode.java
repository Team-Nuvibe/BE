package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ImageResultCode implements ResultCode {

    IMAGE_UPLOAD_OK(HttpStatus.CREATED,"IMAGE001","이미지 업로드에 성공했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
