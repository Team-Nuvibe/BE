package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScrapedImageErrorCode implements ErrorCode {

    SCRAPEDIMAGE_CURSOR_ERROR(HttpStatus.BAD_REQUEST, "SCRAPEDIMAGE001", "유효하지 않은 커서입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
