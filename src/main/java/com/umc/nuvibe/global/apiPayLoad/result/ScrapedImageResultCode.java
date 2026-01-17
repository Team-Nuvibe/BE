package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScrapedImageResultCode implements ResultCode {

    SCRAPEDIMAGE_CREATED(HttpStatus.CREATED, "SCRAPEDIMAGE001", "이미지 스크랩 성공"),
    SCRAPEDIMAGE_DELETED(HttpStatus.OK, "SCRAPEDIMAGE002", "이미지 스크랩 삭제 성공"),
    SCRAPEDIMAGE_TOTAL_LIST_SUCCESS(HttpStatus.OK, "SCRAPEDIMAGE003", "스크랩 이미지 전체 목록 조회 성공"),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
