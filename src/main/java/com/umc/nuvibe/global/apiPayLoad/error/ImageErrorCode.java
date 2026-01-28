package com.umc.nuvibe.global.apiPayLoad.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

    // S3
    IMAGE_IS_EMPTY(HttpStatus.BAD_REQUEST,"COMMON001","사진이 선택되지 않았습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "COMMON002","올바르지 않은 확장자입니다."),
    EXTENSION_IS_EMPTY(HttpStatus.BAD_REQUEST, "IMAGE003","확장자가 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMAGE004","이미지 업로드에 실패했습니다"),
    IMAGETAG_IS_NULL(HttpStatus.BAD_REQUEST,"IMAGE005","이미지 태그가 입력되지 않았습니다"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE006", "이미지가 존재하지 않습니다."),
    IMAGE_BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE007", "이미지가 저장된 보드 조회에 실패했습니다."),
    IMAGE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "IMAGE008", "해당 이미지에 대한 접근 권한이 없습니다."),
    IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE009","이미지 삭제에 실패했습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
