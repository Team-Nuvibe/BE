package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecapResultCode implements ResultCode {

    RECAP_TAG_SUCCESS(HttpStatus.OK, "RECAP001", "태그 리캡 조회에 성공했습니다."),
    RECAP_ARCHIVE_SUCCESS(HttpStatus.OK, "RECAP002", "보드 리캡 조회에 성공했습니다."),
    RECAP_STATUS_SUCCESS(HttpStatus.OK, "RECAP003", "사용자 상태 조회에 성공했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
