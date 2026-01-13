package com.umc.nuvibe.global.apiPayLoad.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ArchiveResultCode implements ResultCode {

    // 조회
    BOARD_LIST_SUCCESS(HttpStatus.OK, "ARCHIVE001", "보드 목록 조회 성공"),
    BOARD_DETAIL_SUCCESS(HttpStatus.OK, "ARCHIVE002", "보드 상세 조회 성공"),
    // 생성
    BOARD_CREATE_SUCCESS(HttpStatus.CREATED, "ARCHIVE003", "보드 생성 성공"),
    // 수정
    BOARD_NAME_UPDATE_SUCCESS(HttpStatus.OK, "ARCHIVE004", "보드명 수정 성공"),
    // 삭제
    BOARD_DELETE_SUCCESS(HttpStatus.OK, "ARCHIVE005", "보드 삭제 성공"),
    BOARD_IMAGE_DELETE_SUCCESS(HttpStatus.OK, "ARCHIVE006", "이미지 삭제 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
