package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HomeResultCode implements ResultCode {

    DROP_MISSION_SUCCESS(HttpStatus.OK, "HOME001", "드롭 미션 조회 성공"),
    MY_BOARDS_SUCCESS(HttpStatus.OK, "HOME002", "나의 기록 조회 성공"),
    CATEGORY_TAGS_SUCCESS(HttpStatus.OK, "HOME003", "카테고리별 태그 조회 성공"),
    TAG_DETAIL_SUCCESS(HttpStatus.OK, "HOME004", "태그 상세 조회 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
