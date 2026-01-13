package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ArchiveErrorCode implements ErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "ARCHIVE001", "해당 보드를 찾을 수 없습니다."),
    BOARD_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARCHIVE002", "해당 이미지를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ARCHIVE003", "사용자를 찾을 수 없습니다."),
    DUPLICATE_BOARD_NAME(HttpStatus.CONFLICT, "ARCHIVE004", "이미 존재하는 보드 이름입니다."),
    BOARD_ACCESS_DENIED(HttpStatus.FORBIDDEN, "ARCHIVE005", "해당 보드에 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}