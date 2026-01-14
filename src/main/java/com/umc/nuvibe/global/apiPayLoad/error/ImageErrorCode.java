package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

    // S3
    IMAGE_IS_EMPTY(HttpStatus.BAD_REQUEST,"COMMON001","사진이 선택되지 않았습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "COMMON002","옳바르지 않은 확장자입니다."),
    EXTENSION_IS_EMPTY(HttpStatus.BAD_REQUEST, "IMAGE003","확장자가 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMAGE004","이미지 업로드에 실패했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
