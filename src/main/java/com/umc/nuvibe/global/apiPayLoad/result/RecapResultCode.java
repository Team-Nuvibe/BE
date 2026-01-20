package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecapResultCode implements ResultCode {

    RECAP_TAG_SUCCESS(HttpStatus.OK, "RECAP001", "태그 리캡 조회에 성공했습니다."),
    RECAP_ARCHIVE_SUCCESS(HttpStatus.OK, "RECAP002", "보드 리캡 조회에 성공했습니다."),
    RECAP_STATUS_SUCCESS(HttpStatus.OK, "RECAP003", "사용자 상태 조회에 성공했습니다."),
    RECAP_CALENDAR_SUCCESS(HttpStatus.OK, "RECAP004", "업로드 날자 조회에 성공했습니다. "),
    RECAP_IMAGES_SUCCESS(HttpStatus.OK, "RECAP005", "해당 날자 이미지 조회에 성공했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
