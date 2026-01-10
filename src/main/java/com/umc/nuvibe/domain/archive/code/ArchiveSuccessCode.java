package com.umc.nuvibe.domain.archive.code;

import com.umc.nuvibe.global.apiPayLoad.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


// 아카이브 성공 코드
@Getter
@RequiredArgsConstructor
public enum ArchiveSuccessCode implements ResultCode {

    BOARD_LIST_SUCCESS(HttpStatus.OK, "ARCHIVE_200_1", "보드 목록 조회 성공"),
    BOARD_DETAIL_SUCCESS(HttpStatus.OK, "ARCHIVE_200_2", "보드 상세 조회 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
